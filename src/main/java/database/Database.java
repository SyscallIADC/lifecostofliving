package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static volatile Database instance;
    private static final String DATABASE_PATH = "jdbc:sqlite:database.db";
    private Connection connection;

    private Database() {
        try {
            connection = DriverManager.getConnection(DATABASE_PATH);
            createTables();
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) instance = new Database();
            }
        }
        return instance;
    }

    Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DATABASE_PATH);
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
        return connection;
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS exchange_rate (" +
                "from_currency TEXT NOT NULL, " +
                "to_currency TEXT NOT NULL, " +
                "exchange_rate REAL, " +
                "last_refreshed TEXT, " +
                "time_zone TEXT, " +
                "PRIMARY KEY (from_currency, to_currency, last_refreshed)" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table exchange_rate: " + e.getMessage());
        }
    }
}
