package database;

import models.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatePersistance implements DatabaseHelper<ExchangeRate> {

    private Connection getConnection() {
        return Database.getInstance().getConnection();
    }

    private ExchangeRate mapRow(ResultSet result) throws SQLException {
        return new ExchangeRate(
                result.getString("from_currency"),
                result.getString("to_currency"),
                result.getDouble("exchange_rate"),
                result.getString("last_refreshed"),
                result.getString("time_zone")
        );
    }

    @Override
    public void insertData(ExchangeRate rate) {
        String sql = "INSERT INTO exchange_rate (from_currency, to_currency, exchange_rate, last_refreshed, time_zone) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, rate.getFromCurrency());
            pstmt.setString(2, rate.getToCurrency());
            pstmt.setDouble(3, rate.getExchangeRate());
            pstmt.setString(4, rate.getLastRefreshed());
            pstmt.setString(5, rate.getTimeZone());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to insert exchange rate: " + e.getMessage());
        }
    }

    @Override
    public List<ExchangeRate> retrieveDataByDate(String date) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String sql = "SELECT * FROM exchange_rate WHERE last_refreshed LIKE ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, date + "%");
            ResultSet result = pstmt.executeQuery();
            while (result.next()) exchangeRates.add(mapRow(result));
        } catch (SQLException e) {
            System.out.println("Failed to retrieve data by date: " + e.getMessage());
        }
        return exchangeRates;
    }

    @Override
    public ExchangeRate retrieveLastData() {
        String sql = "SELECT * FROM exchange_rate ORDER BY last_refreshed DESC LIMIT 1";
        try (Statement statement = getConnection().createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            if (result.next()) return mapRow(result);
        } catch (SQLException e) {
            System.out.println("Failed to retrieve last data: " + e.getMessage());
        }
        return null;
    }
}