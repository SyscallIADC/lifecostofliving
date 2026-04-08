package database;
import models.ExchangeRate;
import models.LivingCost;
import java.util.ArrayList;

import java.sql.*;
import java.util.List;

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
        try (Statement statement = getConnection().createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table exchage_rate: " + e.getMessage());
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
        try (Statement statement = getConnection().createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table cost_of_living: " + e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS scrapper_queue(" +
                "country TEXT PRIMARY KEY," +
                "last_scrapped TEXT" +
                ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table scrapper_queue: " + e.getMessage());
        }

        startQueue();
    }

    public void insertExchangeRate(ExchangeRate rate){
        String sql = "INSERT INTO exchange_rate " +
                "(from_currency, to_currency, exchange_rate, last_refreshed, time_zone)" +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
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

    public void insertCountryToQueue(String country){
        String sql = "INSERT OR IGNORE INTO scrapper_queue (country, last_scrapped) VALUES (?, '0')";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)){
            statement.setString(1, country);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to insert scrapper_queue: " + e.getMessage());
        }
    }

    public void startQueue() {
        List<String> initialCountries = List.of(
                "Spain",
                "France",
                "Switzerland",
                "Germany",
                "United Kingdom",
                "Italy",
                "Greece",
                "Finland",
                "Sweden",
                "Norway",
                "Austria",
                "Russia",
                "Andorra",
                "China",
                "Japan",
                "Australia",
                "United States",
                "Canada",
                "Mexico",
                "Argentina",
                "Peru",
                "Brazil",
                "Ireland",
                "Netherlands",
                "Thailand",
                "India",
                "South Korea",
                "United Arab Emirates",
                "Portugal",
                "Cayman Islands",
                "Dominican Republic",
                "Panama",
                "Qatar",
                "Israel",
                "South Africa"
        );

        for (String country : initialCountries){
            insertCountryToQueue(country);
        }
    }

    public String getNextCountry() {
        String sql = "SELECT country FROM scrapper_queue ORDER BY last_scrapped ASC LIMIT 1";

        try (Statement statement = getConnection().createStatement();
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
        try (PreparedStatement statement = getConnection().prepareStatement(sql)){
            statement.setString(1, country);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to set country as scrapped: " + e.getMessage());
        }
    }

    public List<LivingCost> getLastInserts() {
        List<LivingCost> livingCosts = new ArrayList<>();

        String sql = "SELECT * FROM cost_of_living ORDER BY date DESC, time DESC LIMIT 2";
        try (Statement statement = getConnection().createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                java.util.Map<String, Object> rowMap = new java.util.HashMap<>();

                rowMap.put("country", result.getString("country"));
                rowMap.put("currency", result.getString("currency"));
                rowMap.put("capuccino", result.getDouble("capuccino"));
                rowMap.put("milk", result.getDouble("milk"));
                rowMap.put("bread", result.getDouble("bread"));
                rowMap.put("rice", result.getDouble("rice"));
                rowMap.put("eggs", result.getDouble("eggs"));
                rowMap.put("cheese", result.getDouble("cheese"));
                rowMap.put("chicken", result.getDouble("chicken"));
                rowMap.put("beef", result.getDouble("beef"));
                rowMap.put("fruits", result.getDouble("fruits"));
                rowMap.put("vegetables", result.getDouble("vegetables"));
                rowMap.put("water", result.getDouble("water"));
                rowMap.put("public_transport", result.getDouble("public_transport"));
                rowMap.put("gasoline", result.getDouble("gasoline"));
                rowMap.put("car", result.getDouble("car"));
                rowMap.put("utilities", result.getDouble("utilities"));
                rowMap.put("child_care", result.getDouble("child_care"));
                rowMap.put("gym_monthly", result.getDouble("gym_monthly"));
                rowMap.put("bedroom_month", result.getDouble("bedroom_month"));
                rowMap.put("appartment_month", result.getDouble("appartment_month"));
                rowMap.put("appartment_buy", result.getDouble("appartment_buy"));
                rowMap.put("salary_month", result.getDouble("salary_month"));
                rowMap.put("interest_rate_twenty_years", result.getDouble("interest_rate_twenty_years"));
                rowMap.put("date", result.getString("date"));
                rowMap.put("time", result.getString("time"));

                livingCosts.add(LivingCost.fromMap(rowMap));
            }
        } catch (SQLException e) {
            System.out.println("Failed to getLastInserts: " + e.getMessage());
        }
        return livingCosts;
    }
}



