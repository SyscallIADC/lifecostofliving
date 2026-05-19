package org.syscall.livingcost.control.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    protected static void initialize(String databasePath){
        createTable(databasePath);
    }
    private static void createTable(String databasePath) {
        String sql = "CREATE TABLE IF NOT EXISTS cost_of_living (" +
                "country TEXT NOT NULL, " +
                "currency TEXT NOT NULL, " +
                "cappuccino REAL, " +
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
                "apartment_month REAL, " +
                "apartment_buy REAL, " +
                "salary_month REAL, " +
                "interest_rate REAL, " +
                "date TEXT NOT NULL, " +
                "time TEXT NOT NULL, " +
                "PRIMARY KEY (country, date, time)" +
                ");";
        try (Connection connection = DatabaseHelper.getConnection(databasePath);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table cost_of_living: " + e.getMessage());
        }
    }
}
