package org.syscall.livingcost.control.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.syscall.livingcost.control.database.DatabaseHelper;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseHelperTest {
    @TempDir
    Path tempDir;

    @Test
    void testCreateAndReturnConnection() throws SQLException {
        String databasePath = "jdbc:sqlite:" + tempDir.resolve("test.db");

        assertDoesNotThrow(() -> DatabaseHelper.initialize(databasePath),
                "Initialization should not fail.");
        try (Connection connection = DatabaseHelper.getConnection(databasePath)) {
            assertNotNull(connection, "Connection should not be null.");
            assertFalse(connection.isClosed(), "Connection should not be closed.");
        }
    }

    @Test
    void testThrowsExceptionInvalidPath() {
        String invalidPath = "jdbc:melo:invento";

        assertThrows(SQLException.class, () -> DatabaseHelper.getConnection(invalidPath),
                "Should throw a SQLException");
    }
}
