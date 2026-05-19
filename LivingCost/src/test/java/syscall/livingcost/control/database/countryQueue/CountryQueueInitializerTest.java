package syscall.livingcost.control.database.countryQueue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.syscall.livingcost.control.database.DatabaseHelper;
import org.syscall.livingcost.control.database.countryQueue.CountryQueueInitializer;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryQueueInitializerTest {
    @TempDir
    Path tempDir;

    @Test
    void testInitializeCountryQueue() throws Exception {
        String databasePath = "jdbc:sqlite:" + tempDir.resolve("test.db");
        CountryQueueInitializer.initialize(databasePath);
        String sql = "SELECT COUNT(*) FROM scrapper_queue";

        try (Connection connection = DatabaseHelper.getConnection(databasePath);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int countryCount = rs.getInt(1);
            assertEquals(35, countryCount, "Should have initialized 35 countries in the queue.");
        }
    }
}
