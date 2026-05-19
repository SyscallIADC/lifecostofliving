package org.syscall.business.control.datamart;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.syscall.business.model.CountryStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatamartDAO implements DatamartRepository {

    @Override
    public void upsertCountryData(ParsedEvent event) {
        JsonObject root = JsonParser.parseString(event.rawJson()).getAsJsonObject();
        JsonObject data = root.getAsJsonObject("data");

        String timestamp = root.get("ts").getAsString();
        String countryName = data.get("country").getAsString();

        String sql = """
            INSERT OR REPLACE INTO LivingCostMetrics 
            (country, capture_timestamp, currency, cappuccino, milk, rice, bread, eggs, cheese, chicken, beef, 
             fruits, vegetables, water, publicTransport, gasoline, car, utilities, childCare, 
             gymMonthly, bedroomMonth, apartmentMonth, apartmentBuy, salaryMonth, interestRate) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, countryName);
            pstmt.setString(2, timestamp);
            pstmt.setString(3, data.get("currency").getAsString());
            pstmt.setDouble(4, data.get("cappuccino").getAsDouble());
            pstmt.setDouble(5, data.get("milk").getAsDouble());
            pstmt.setDouble(6, data.get("rice").getAsDouble());
            pstmt.setDouble(7, data.get("bread").getAsDouble());
            pstmt.setDouble(8, data.get("eggs").getAsDouble());
            pstmt.setDouble(9, data.get("cheese").getAsDouble());
            pstmt.setDouble(10, data.get("chicken").getAsDouble());
            pstmt.setDouble(11, data.get("beef").getAsDouble());
            pstmt.setDouble(12, data.get("fruits").getAsDouble());
            pstmt.setDouble(13, data.get("vegetables").getAsDouble());
            pstmt.setDouble(14, data.get("water").getAsDouble());
            pstmt.setDouble(15, data.get("publicTransport").getAsDouble());
            pstmt.setDouble(16, data.get("gasoline").getAsDouble());
            pstmt.setDouble(17, data.get("car").getAsDouble());
            pstmt.setDouble(18, data.get("utilities").getAsDouble());
            pstmt.setDouble(19, data.get("childCare").getAsDouble());
            pstmt.setDouble(20, data.get("gymMonthly").getAsDouble());
            pstmt.setDouble(21, data.get("bedroomMonth").getAsDouble());
            pstmt.setDouble(22, data.get("apartmentMonth").getAsDouble());
            pstmt.setDouble(23, data.get("apartmentBuy").getAsDouble());
            pstmt.setDouble(24, data.get("salaryMonth").getAsDouble());
            pstmt.setDouble(25, data.get("interestRate").getAsDouble());

            pstmt.executeUpdate();

        } catch (SQLException | NullPointerException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void upsertExchangeRateData(ParsedEvent event) {
        JsonObject root = JsonParser.parseString(event.rawJson()).getAsJsonObject();
        JsonObject data = root.getAsJsonObject("data");

        String timestamp = root.get("ts").getAsString();
        String fromCurrency = data.get("fromCurrency").getAsString();
        String toCurrency = data.get("toCurrency").getAsString();
        String currencyPair = fromCurrency + "_" + toCurrency;

        String sql = """
            INSERT OR REPLACE INTO ExchangeRates 
            (currency_pair, capture_timestamp, from_currency, to_currency, exchange_rate, last_refreshed) 
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currencyPair);
            pstmt.setString(2, timestamp);
            pstmt.setString(3, fromCurrency);
            pstmt.setString(4, toCurrency);
            pstmt.setDouble(5, data.get("exchangeRate").getAsDouble());
            pstmt.setString(6, data.get("lastRefreshed").getAsString());

            pstmt.executeUpdate();

        } catch (SQLException | NullPointerException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<CountryStats> getCountryHistory(String countryName, int limit) {
        List<CountryStats> list = new ArrayList<>();
        String sql = "SELECT * FROM LivingCostMetrics WHERE country = ? COLLATE NOCASE ORDER BY capture_timestamp DESC LIMIT ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, countryName);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToStats(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Double> getExchangeRateHistory(String fromCurrency, String toCurrency, int limit) {
        List<Double> list = new ArrayList<>();
        if (fromCurrency.equalsIgnoreCase(toCurrency)) return list;

        String pair = fromCurrency.toUpperCase() + "_" + toCurrency.toUpperCase();
        String sql = "SELECT exchange_rate FROM ExchangeRates WHERE currency_pair = ? ORDER BY capture_timestamp DESC LIMIT ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pair);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getDouble("exchange_rate"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        if (list.isEmpty()) {
            String inversePair = toCurrency.toUpperCase() + "_" + fromCurrency.toUpperCase();
            String sqlInv = "SELECT exchange_rate FROM ExchangeRates WHERE currency_pair = ? ORDER BY capture_timestamp DESC LIMIT ?";
            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sqlInv)) {
                pstmt.setString(1, inversePair);
                pstmt.setInt(2, limit);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    list.add(1.0 / rs.getDouble("exchange_rate"));
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return list;
    }

    @Override
    public CountryStats getCountryStats(String countryName) {
        String sql = "SELECT * FROM LivingCostMetrics WHERE country = ? COLLATE NOCASE ORDER BY capture_timestamp DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, countryName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStats(rs);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<CountryStats> getAllCountries() {
        List<CountryStats> list = new ArrayList<>();
        String sql = """
            SELECT a.* FROM LivingCostMetrics a
            INNER JOIN (
                SELECT country, MAX(capture_timestamp) as max_ts
                FROM LivingCostMetrics
                GROUP BY country
            ) b ON a.country = b.country AND a.capture_timestamp = b.max_ts
        """;
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToStats(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    @Override
    public double getExchangeRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equalsIgnoreCase(toCurrency)) return 1.0;

        String pair = fromCurrency.toUpperCase() + "_" + toCurrency.toUpperCase();
        String sql = "SELECT exchange_rate FROM ExchangeRates WHERE currency_pair = ? ORDER BY capture_timestamp DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pair);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble("exchange_rate");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        String inversePair = toCurrency.toUpperCase() + "_" + fromCurrency.toUpperCase();
        String sqlInv = "SELECT exchange_rate FROM ExchangeRates WHERE currency_pair = ? ORDER BY capture_timestamp DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInv)) {
            pstmt.setString(1, inversePair);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return 1.0 / rs.getDouble("exchange_rate");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return 1.0;
    }

    private CountryStats mapResultSetToStats(ResultSet rs) throws SQLException {
        String countryCurrency = rs.getString("currency");
        double rateToEur = getExchangeRate(countryCurrency, "EUR");
        return CountryStatsFactory.create(rs, rateToEur);
    }
}