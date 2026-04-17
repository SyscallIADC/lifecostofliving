package syscall.livingcost.control.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import syscall.livingcost.control.database.DatabaseHelper;
import syscall.livingcost.control.database.DatabaseInitializer;

import java.nio.file.Path;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseInitializerTest {
    @TempDir
    Path tempDir;

    @Test
    void testCreateTable() throws SQLException {
        String databasePath = "jdbc:sqlite:" + tempDir.resolve("test.db");
        DatabaseInitializer.initialize(databasePath);

        try(Connection connection = DatabaseHelper.getConnection(databasePath)){
            DatabaseMetaData data = connection.getMetaData();
            try(ResultSet tables = data.getTables(null, null, "cost_of_living", null)) {
                assertTrue(tables.next(), "cost_of_living table should be created");
            }
            try (ResultSet columns = data.getColumns(null, null, "cost_of_living", "cappuccino")) {
                assertTrue(columns.next(), "cappuccino table should be created");
            }
        }
    }

    @Test
    void testNotFailIfTableExists() {
        String dbPath = "jdbc:sqlite:" + tempDir.resolve("test-duplicate.db");
        DatabaseInitializer.initialize(dbPath);
        DatabaseInitializer.initialize(dbPath);
    }
}
