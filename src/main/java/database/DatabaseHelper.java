package database;
import models.ExchangeRate;
import models.LivingCost;

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
        String sql = "CREATE TABLE IF NOT EXISTS exchange_rate (" +
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

        sql = "CREATE TABLE IF NOT EXISTS cost_of_living (" +
                "country TEXT NOT NULL, " +
                "currency TEXT NOT NULL, " +
                "capuccino REAL, " +
                "milk REAL, " +
                "bread REAL, " +
                "rice REAL, " +
                "eggs REAL, " +
                "cheese REAL, " +
                "chicken REAL, " +
                "beef REAL, " +
                "fruits REAL, " +
                "vegetables REAL, " +
                "water REAL, " +
                "public_transport REAL, " +
                "gasoline REAL, " +
                "car REAL, " +
                "utilities REAL, " +
                "child_care REAL, " +
                "gym_monthly REAL, " +
                "bedroom_month REAL, " +
                "appartment_month REAL, " +
                "appartment_buy REAL, " +
                "salary_month REAL, " +
                "interest_rate_twenty_years REAL, " +
                "date TEXT NOT NULL, " +
                "time TEXT NOT NULL, " +
                "PRIMARY KEY (country, date, time)" +
                ");";
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

    public void insertCostOfLiving(LivingCost cost) {
        String sql = "INSERT INTO cost_of_living (" +
                "country, currency, capuccino, milk, bread, rice, eggs, " +
                "cheese, chicken, beef, fruits, vegetables, water, " +
                "public_transport, gasoline, car, utilities, child_care, " +
                "gym_monthly, bedroom_month, appartment_month, appartment_buy, " +
                "salary_month, interest_rate_twenty_years, date, time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, cost.getCountry());
            pstmt.setString(2, cost.getCurrency());
            pstmt.setDouble(3, cost.getCapuccino());
            pstmt.setDouble(4, cost.getMilk());
            pstmt.setDouble(5, cost.getBread());
            pstmt.setDouble(6, cost.getRice());
            pstmt.setDouble(7, cost.getEggs());
            pstmt.setDouble(8, cost.getCheese());
            pstmt.setDouble(9, cost.getChicken());
            pstmt.setDouble(10, cost.getBeef());
            pstmt.setDouble(11, cost.getFruits());
            pstmt.setDouble(12, cost.getVegetables());
            pstmt.setDouble(13, cost.getWater());
            pstmt.setDouble(14, cost.getPublicTransport());
            pstmt.setDouble(15, cost.getGasoline());
            pstmt.setDouble(16, cost.getCar());
            pstmt.setDouble(17, cost.getUtilities());
            pstmt.setDouble(18, cost.getChildCare());
            pstmt.setDouble(19, cost.getGymMonthly());
            pstmt.setDouble(20, cost.getBedroomMonth());
            pstmt.setDouble(21, cost.getAppartmentMonth());
            pstmt.setDouble(22, cost.getAppartmentBuy());
            pstmt.setDouble(23, cost.getSalaryMonth());
            pstmt.setDouble(24, cost.getInterestRateTwentyYears());
            pstmt.setString(25, cost.getDate().toString());
            pstmt.setString(26, cost.getTime().toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting cost of living: " + e.getMessage());
        }
    }
}



