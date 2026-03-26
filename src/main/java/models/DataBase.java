package models;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

    private static DataBase instance;
    private Connection connection;

    private static final String URL = "jdbc:sqlite:CostOfLiving.db";


    private DataBase() {
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}