package database;
import models.ExchangeRate;

import java.sql.*;

public class DatabaseHelper{
    private static volatile DatabaseHelper instance;
    private static final String DATABASE_PATH = "jdbc:sqlite:database.db";
    private Connection connection;
    private DatabaseHelper(){
        try {
            connection = DriverManager.getConnection(DATABASE_PATH);
            createTables();
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null | connection.isClosed()) {
                connection = DriverManager.getConnection(DATABASE_PATH);
            }
        } catch(SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
        return connection;
    }

    private void createTables() {
        String sql = "CREATE TABLE [IF NOT EXISTS] exchange-rate (" +
                "from_currency TEXT NOT NULL, " +
                "to_currency TEXT NOT NULL, " +
                "exchange_rate REAL, " +
                "last_refreshed TEXT, " +
                "time_zone TEXT," +
                "PRIMARY KEY (from_currency, to_currency, last_refreshed)" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table exchage-rate: " + e.getMessage());
        }

        sql = "CREATE TABLE [IF NOT EXISTS] cost-of-living (" +
                "" +
                "";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table cost-of-living: " + e.getMessage());
        }
    }

    public void insertExchangeRate(ExchangeRate rate){
        String sql = "INSERT INTO exchange-rate " +
                "(from_currency, to_currency, exchange_rate, last_refreshed, time_zone)" +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rate.getFromCurrency());
            pstmt.setString(2, rate.getToCurrency());
            pstmt.setDouble(3, rate.getExchangeRate());
            pstmt.setString(4, rate.getLastRefreshed());
            pstmt.setString(5, rate.getTimeZone());
        pstmt.executeUpdate();

        }catch (SQLException e){
            System.out.println("Failed to insert exchange rate: " + e.getMessage());

        }
    }
}



