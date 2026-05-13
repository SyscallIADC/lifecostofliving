package syscall.livingcost.control.database;

import syscall.livingcost.model.LivingCost;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteLivingCostStore implements LivingCostStore {
    private final String databasePath;
    public SQLiteLivingCostStore(String databasePath){
        this.databasePath = databasePath;
    }

    @Override
    public void insertData(LivingCost cost) {
        String sql = "INSERT INTO cost_of_living (" +
                "country, currency, cappuccino, milk, bread, rice, eggs, " +
                "cheese, chicken, beef, fruits, vegetables, water, " +
                "public_transport, gasoline, car, utilities, child_care, " +
                "gym_monthly, bedroom_month, apartment_month, apartment_buy, " +
                "salary_month, interest_rate, date, time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseHelper.getConnection(databasePath);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cost.country());
            pstmt.setString(2, cost.currency());
            pstmt.setDouble(3, cost.cappuccino());
            pstmt.setDouble(4, cost.milk());
            pstmt.setDouble(5, cost.bread());
            pstmt.setDouble(6, cost.rice());
            pstmt.setDouble(7, cost.eggs());
            pstmt.setDouble(8, cost.cheese());
            pstmt.setDouble(9, cost.chicken());
            pstmt.setDouble(10, cost.beef());
            pstmt.setDouble(11, cost.fruits());
            pstmt.setDouble(12, cost.vegetables());
            pstmt.setDouble(13, cost.water());
            pstmt.setDouble(14, cost.publicTransport());
            pstmt.setDouble(15, cost.gasoline());
            pstmt.setDouble(16, cost.car());
            pstmt.setDouble(17, cost.utilities());
            pstmt.setDouble(18, cost.childCare());
            pstmt.setDouble(19, cost.gymMonthly());
            pstmt.setDouble(20, cost.bedroomMonth());
            pstmt.setDouble(21, cost.apartmentMonth());
            pstmt.setDouble(22, cost.apartmentBuy());
            pstmt.setDouble(23, cost.salaryMonth());
            pstmt.setDouble(24, cost.interestRate());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting cost of living: " + e.getMessage());
        }
    }

    @Override
    public List<LivingCost> retrieveDataByDate(String date) {
        List<LivingCost> livingCosts= new ArrayList<>();

        String sql = "SELECT * FROM cost_of_living WHERE `date` = ?";
        try (Connection connection = DatabaseHelper.getConnection(databasePath);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);

            try (ResultSet result = pstmt.executeQuery()) {
                while (result.next()) {
                    java.util.Map<String, Object> rowMap = new java.util.HashMap<>();

                    rowMap.put("country", result.getString("country"));
                    rowMap.put("currency", result.getString("currency"));
                    rowMap.put("cappuccino", result.getDouble("cappuccino"));
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
                    rowMap.put("apartment_month", result.getDouble("apartment_month"));
                    rowMap.put("apartment_buy", result.getDouble("apartment_buy"));
                    rowMap.put("salary_month", result.getDouble("salary_month"));
                    rowMap.put("interest_rate", result.getDouble("interest_rate"));
                    rowMap.put("date", result.getString("date"));
                    rowMap.put("time", result.getString("time"));

                    livingCosts.add(PersistenceMapper.fromMap(rowMap));
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve data: " + e.getMessage());
        }
        return livingCosts;
    }

    @Override
    public LivingCost retrieveLastData() {
        LivingCost lastCost = null;
        String sql = "SELECT * FROM cost_of_living ORDER BY date DESC, time DESC LIMIT 1";

        try (Connection connection = DatabaseHelper.getConnection(databasePath);
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            if (result.next()) {
                java.util.Map<String, Object> rowMap = new java.util.HashMap<>();

                rowMap.put("country", result.getString("country"));
                rowMap.put("currency", result.getString("currency"));
                rowMap.put("cappuccino", result.getDouble("cappuccino"));
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
                rowMap.put("apartment_month", result.getDouble("apartment_month"));
                rowMap.put("apartment_buy", result.getDouble("apartment_buy"));
                rowMap.put("salary_month", result.getDouble("salary_month"));
                rowMap.put("interest_rate", result.getDouble("interest_rate"));
                rowMap.put("date", result.getString("date"));
                rowMap.put("time", result.getString("time"));

                lastCost = PersistenceMapper.fromMap(rowMap);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la última entrada: " + e.getMessage());
        }

        return lastCost;
    }
}
