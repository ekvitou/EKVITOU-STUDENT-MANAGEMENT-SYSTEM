package com.ekvitou.studentmanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectionConfig {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/university";
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "123";

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        } catch (Exception exception){
            System.out.println("Problem during get connection: " + exception.getMessage());
            return null;
        }
    }
}
