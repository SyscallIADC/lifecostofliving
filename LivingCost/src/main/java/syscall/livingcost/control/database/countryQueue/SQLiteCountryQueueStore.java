package syscall.livingcost.control.database.countryQueue;

import syscall.livingcost.control.database.DatabaseHelper;

import java.sql.*;

public class SQLiteCountryQueueStore implements CountryQueueStore{
    private final String databasePath;

    public SQLiteCountryQueueStore(String databasePath) {
        this.databasePath = databasePath;
    }

    public String getNextCountry() {
        String sql = "SELECT country FROM scrapper_queue ORDER BY last_scrapped ASC LIMIT 1";

        try (Connection connection = DatabaseHelper.getConnection(databasePath);
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)){

            if (result.next()) {
                return result.getString("country");
            }
        } catch (SQLException e) {
            System.out.println("Failed to get nextCountry: " + e.getMessage());
        }
        return null;
    }

    public void setCountryAsScrapped(String country) {
        String sql = "UPDATE scrapper_queue SET last_scrapped = CURRENT_TIMESTAMP WHERE country = ?";
        try (Connection connection = DatabaseHelper.getConnection(databasePath);
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, country);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to set country as scrapped: " + e.getMessage());
        }
    }

    protected static void insertCountryToQueue(String country, String databasePath){
        String sql = "INSERT OR IGNORE INTO scrapper_queue (country, last_scrapped) VALUES (?, '0')";
        try (Connection connection = DatabaseHelper.getConnection(databasePath);
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, country);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to insert scrapper_queue: " + e.getMessage());
        }
    }

    protected static void createTable(String databasePath) {
        String sql = "CREATE TABLE IF NOT EXISTS scrapper_queue(" +
                "country TEXT PRIMARY KEY," +
                "last_scrapped TEXT" +
                ");";
        try (Connection connection = DatabaseHelper.getConnection(databasePath);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table scrapper_queue: " + e.getMessage());
        }
    }
}
