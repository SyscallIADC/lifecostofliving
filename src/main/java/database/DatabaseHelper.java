package database;
import java.sql.*;

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

    public Connection getConnection() {ç
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
        String sql = "CREATE TABLE [IF NOT EXISTS] currencies (" +
                "" +
                "";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table currencies: " + e.getMessage());
        }

        sql = "CREATE TABLE [IF NOT EXISTS] cost-of-living (" +
                "" +
                "";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table cost-of-living: " + e.getMessage());
        }
    }
}
