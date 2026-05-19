package org.syscall.livingcost.control.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    public static void initialize(String databasePath){
        DatabaseInitializer.initialize(databasePath);
    }

    public static Connection getConnection(String databasePath) throws SQLException{
        return DriverManager.getConnection(databasePath);
    }


}
