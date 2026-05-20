package org.syscall.exchangerate.control.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseHelperTest {

    @Test
    void testGetConnectionReturnsValidConnection() throws SQLException {
        try (Connection connection = DatabaseHelper.getConnection()) {
            assertNotNull(connection, "La conexión no debería ser nula");
            assertFalse(connection.isClosed(), "La conexión debería estar abierta");
        }
    }

    @Test
    void testCreateTablesCreatesExchangeRateTable() throws SQLException {
        DatabaseHelper.createTables();
        boolean tableExists = false;

        String checkTableSql = "SELECT name FROM sqlite_master WHERE type='table' AND name='exchange_rate'";

        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(checkTableSql)) {

            if (resultSet.next()) {
                tableExists = true;
            }
        }

        assertTrue(tableExists, "La tabla 'exchange_rate' debería existir en la base de datos");
    }

    @AfterAll
    static void tearDown() {
        File dbFile = new File("database.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }
}