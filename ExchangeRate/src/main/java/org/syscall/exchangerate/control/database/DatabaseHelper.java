package org.syscall.exchangerate.control.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    private static final String DATABASE_PATH = "jdbc:sqlite:database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_PATH);
    }

    public static void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS exchange_rate (" +
                "from_currency TEXT NOT NULL, " +
                "to_currency TEXT NOT NULL, " +
                "exchange_rate REAL, " +
                "last_refreshed TEXT, " +
                "time_zone TEXT, " +
                "PRIMARY KEY (from_currency, to_currency, last_refreshed)" +
                ");";
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table: " + e.getMessage());
        }
    }
}


