package database;

import models.ExchangeRate;
import models.ExchangeRateParser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatePersistance implements DatabaseHelper<ExchangeRate> {
    @Override
    public void insertData(ExchangeRate rate) {
        String sql = "INSERT INTO exchange_rate (from_currency, to_currency, exchange_rate, last_refreshed, time_zone)VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.getConnection().prepareStatement(sql)) {
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
    public void retrieveDataByDate(String date){
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        ExchangeRateParser parser = new ExchangeRateParser();

        String sql = "SELECT * FROM exchange_rate WHERE date = " + date;
        try(Statement statement = Database.getInstance().getConnection().createStatement();
        ResultSet result = statement.executeQuery(sql)){
            while(result.next()){
                java.util.Map<String, Object> rowMap = new java.util.HashMap<>();

                rowMap.put("from_currency", result.getString("from_currency"));
                rowMap.put("to_currency", result.getString("to_currency"));
                rowMap.put("exchange_rate", result.getString("exchange_rate"));
                rowMap.put("last_refreshed", result.getString("last_refreshed"));
                rowMap.put("time_zone", result.getString("time_zone"));

                exchangeRates.add(parser.fromMap(rowMap));

            } catch (SQLException e){
                System.out.println("Failed to retrieve data: " + e.getMessage());
            }
            return exchangeRates;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
