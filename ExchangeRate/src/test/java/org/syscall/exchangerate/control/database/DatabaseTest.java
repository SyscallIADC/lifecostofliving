package org.syscall.exchangerate.control.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    void testSingletonReturnsSameInstance() {
        Database instance1 = Database.getInstance();
        Database instance2 = Database.getInstance();

        assertNotNull(instance1, "La instancia no debería ser nula");
        assertSame(instance1, instance2, "El patrón Singleton falla: se han devuelto instancias diferentes");
    }

    @Test
    void testGetConnectionReturnsOpenConnection() throws SQLException {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();

        assertNotNull(connection, "La conexión no debería ser nula");
        assertFalse(connection.isClosed(), "La conexión debería estar abierta");
    }

    @Test
    void testTablesAreCreatedOnInitialization() throws SQLException {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        boolean tableExists = false;

        String checkTableSql = "SELECT name FROM sqlite_master WHERE type='table' AND name='exchange_rate'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(checkTableSql)) {

            if (resultSet.next()) {
                tableExists = true;
            }
        }

        assertTrue(tableExists, "La tabla 'exchange_rate' debería existir en la base de datos");
    }

    @AfterAll
    static void tearDown() {
    }
}