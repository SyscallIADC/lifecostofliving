package org.syscall.business.control.datamart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:business_datamart.db";

    public static void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS LivingCostMetrics (
                country TEXT,
                capture_timestamp TEXT,
                currency TEXT,
                cappuccino REAL,
                milk REAL,
                rice REAL,
                bread REAL,
                eggs REAL,
                cheese REAL,
                chicken REAL,
                beef REAL,
                fruits REAL,
                vegetables REAL,
                water REAL,
                publicTransport REAL,
                gasoline REAL,
                car REAL,
                utilities REAL,
                childCare REAL,
                gymMonthly REAL,
                bedroomMonth REAL,
                apartmentMonth REAL,
                apartmentBuy REAL,
                salaryMonth REAL,
                interestRate REAL,
                PRIMARY KEY (country, capture_timestamp)
            );
        """;

        String sqlExchangeRates = """
            CREATE TABLE IF NOT EXISTS ExchangeRates (
                currency_pair TEXT,
                capture_timestamp TEXT,
                from_currency TEXT,
                to_currency TEXT,
                exchange_rate REAL,
                last_refreshed TEXT,
                PRIMARY KEY (currency_pair, capture_timestamp)
            );
        """;

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            stmt.execute(sqlExchangeRates);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
