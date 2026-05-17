package org.syscall.business.control.datamart;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.*;

public class DatamartDAO implements DatamartRepository {
    private static final String URL = "jdbc:sqlite:business_datamart.db";

    public DatamartDAO() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS LivingCostMetrics (
                country TEXT PRIMARY KEY,
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
                interestRate REAL
            );
        """;

        String sqlExchangeRates = """
            CREATE TABLE IF NOT EXISTS ExchangeRates (
                currency_pair TEXT PRIMARY KEY,
                from_currency TEXT,
                to_currency TEXT,
                exchange_rate REAL,
                last_refreshed TEXT
            );
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.execute(sqlExchangeRates);
            System.out.println("Datamart SQLite inicializado correctamente con LivingCost y ExchangeRate.");
        } catch (SQLException e) {
            System.err.println("Error creando la tabla: " + e.getMessage());
        }
    }

    @Override
    public void upsertCountryData(ParsedEvent event) {
        JsonObject root = JsonParser.parseString(event.rawJson()).getAsJsonObject();
        JsonObject data = root.getAsJsonObject("data");

        String countryName = data.get("country").getAsString();

        String sql = """
            INSERT OR REPLACE INTO LivingCostMetrics 
            (country, currency, cappuccino, milk, rice, bread, eggs, cheese, chicken, beef, 
             fruits, vegetables, water, publicTransport, gasoline, car, utilities, childCare, 
             gymMonthly, bedroomMonth, apartmentMonth, apartmentBuy, salaryMonth, interestRate) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, countryName);
            pstmt.setString(2, data.get("currency").getAsString());
            pstmt.setDouble(3, data.get("cappuccino").getAsDouble());
            pstmt.setDouble(4, data.get("milk").getAsDouble());
            pstmt.setDouble(5, data.get("rice").getAsDouble());
            pstmt.setDouble(6, data.get("bread").getAsDouble());
            pstmt.setDouble(7, data.get("eggs").getAsDouble());
            pstmt.setDouble(8, data.get("cheese").getAsDouble());
            pstmt.setDouble(9, data.get("chicken").getAsDouble());
            pstmt.setDouble(10, data.get("beef").getAsDouble());
            pstmt.setDouble(11, data.get("fruits").getAsDouble());
            pstmt.setDouble(12, data.get("vegetables").getAsDouble());
            pstmt.setDouble(13, data.get("water").getAsDouble());
            pstmt.setDouble(14, data.get("publicTransport").getAsDouble());
            pstmt.setDouble(15, data.get("gasoline").getAsDouble());
            pstmt.setDouble(16, data.get("car").getAsDouble());
            pstmt.setDouble(17, data.get("utilities").getAsDouble());
            pstmt.setDouble(18, data.get("childCare").getAsDouble());
            pstmt.setDouble(19, data.get("gymMonthly").getAsDouble());
            pstmt.setDouble(20, data.get("bedroomMonth").getAsDouble());
            pstmt.setDouble(21, data.get("apartmentMonth").getAsDouble());
            pstmt.setDouble(22, data.get("apartmentBuy").getAsDouble());
            pstmt.setDouble(23, data.get("salaryMonth").getAsDouble());
            pstmt.setDouble(24, data.get("interestRate").getAsDouble());

            pstmt.executeUpdate();

        } catch (SQLException | NullPointerException e) {
            System.err.println("Error actualizando datos de " + countryName + ": " + e.getMessage());
        }
    }

    @Override
    public void upsertExchangeRateData(ParsedEvent event) {
        JsonObject root = JsonParser.parseString(event.rawJson()).getAsJsonObject();
        JsonObject data = root.getAsJsonObject("data");

        String fromCurrency = data.get("fromCurrency").getAsString();
        String toCurrency = data.get("toCurrency").getAsString();
        String currencyPair = fromCurrency + "_" + toCurrency;

        String sql = """
            INSERT OR REPLACE INTO ExchangeRates 
            (currency_pair, from_currency, to_currency, exchange_rate, last_refreshed) 
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currencyPair);
            pstmt.setString(2, fromCurrency);
            pstmt.setString(3, toCurrency);
            pstmt.setDouble(4, data.get("exchangeRate").getAsDouble());
            pstmt.setString(5, data.get("lastRefreshed").getAsString());

            pstmt.executeUpdate();

        } catch (SQLException | NullPointerException e) {
            System.err.println("Error actualizando divisa " + currencyPair + ": " + e.getMessage());
        }
    }
}