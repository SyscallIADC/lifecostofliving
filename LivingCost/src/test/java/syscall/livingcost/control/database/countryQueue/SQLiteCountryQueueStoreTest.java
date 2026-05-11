package syscall.livingcost.control.database.countryQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SQLiteCountryQueueStoreTest {
    private SQLiteCountryQueueStore store;
    private String databasePath;

    @TempDir
    Path tempDir;


    @BeforeEach
    void setUp(){
        databasePath = "jdbc:sqlite:" + tempDir.resolve("test.db");
        SQLiteCountryQueueStore.createTable(databasePath);
        store = new SQLiteCountryQueueStore(databasePath);
    }

    @Test
    void testEmptyQueue(){
        String next = store.getNextCountry();
        assertNull(next, "Should return null.");
    }

    @Test
    void testInsertAndIgnoreCountries(){
        SQLiteCountryQueueStore.insertCountryToQueue("Spain", databasePath);
        SQLiteCountryQueueStore.insertCountryToQueue("Spain", databasePath);
        SQLiteCountryQueueStore.insertCountryToQueue("Peru", databasePath);

        String first = store.getNextCountry();
        assertNotNull(first);
        assertTrue(first.equals("Spain") || first.equals("Peru"));
    }

    @Test
    void testRotationWhenScrapped() {
        SQLiteCountryQueueStore.insertCountryToQueue("Spain", databasePath);
        SQLiteCountryQueueStore.insertCountryToQueue("France", databasePath);

        String firstTurn = store.getNextCountry();
        assertNotNull(firstTurn);

        String secondTurn = firstTurn.equals("Spain") ? "France" : "Spain";

        store.setCountryAsScrapped(firstTurn);

        String realSecondTurn = store.getNextCountry();

        assertEquals(realSecondTurn, secondTurn);
    }
}
