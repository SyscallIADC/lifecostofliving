package database;

import models.ExchangeRate;
import models.ExchangeRateParser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeRatePersistance implements DatabaseHelper<ExchangeRate> {

    @Override
    public void insertData(ExchangeRate rate) {
        String sql = "INSERT INTO exchange_rate (from_currency, to_currency, exchange_rate, last_refreshed, time_zone) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = Database.getInstance().getConnection().prepareStatement(sql)) {
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
    public List<ExchangeRate> retrieveDataByDate(String date) {  // ← void → List<ExchangeRate>
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        ExchangeRateParser parser = new ExchangeRateParser();

        String sql = "SELECT * FROM exchange_rate WHERE last_refreshed LIKE ?";

        try (PreparedStatement pstmt = Database.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, date + "%");  // así busca por fecha aunque tenga hora
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {  // ← ahora las llaves están correctas
                Map<String, Object> rowMap = new HashMap<>();
                rowMap.put("from_currency", result.getString("from_currency"));
                rowMap.put("to_currency", result.getString("to_currency"));
                rowMap.put("exchange_rate", result.getDouble("exchange_rate")); // ← getDouble, no getString
                rowMap.put("last_refreshed", result.getString("last_refreshed"));
                rowMap.put("time_zone", result.getString("time_zone"));
                exchangeRates.add(parser.fromMap(rowMap));
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve data: " + e.getMessage());
        }

        return exchangeRates;
    }

    @Override
    public ExchangeRate retrieveLastData() {  // ← método que faltaba implementar
        ExchangeRateParser parser = new ExchangeRateParser();
        String sql = "SELECT * FROM exchange_rate ORDER BY last_refreshed DESC LIMIT 1";

        try (Statement statement = Database.getInstance().getConnection().createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            if (result.next()) {
                Map<String, Object> rowMap = new HashMap<>();
                rowMap.put("from_currency", result.getString("from_currency"));
                rowMap.put("to_currency", result.getString("to_currency"));
                rowMap.put("exchange_rate", result.getDouble("exchange_rate"));
                rowMap.put("last_refreshed", result.getString("last_refreshed"));
                rowMap.put("time_zone", result.getString("time_zone"));
                return parser.fromMap(rowMap);
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve last data: " + e.getMessage());
        }

        return null;
    }
}