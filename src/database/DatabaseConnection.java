package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/osmanyhall"; // Your Database Name
    private static final String USER = "root"; // Your MySQL Username
    private static final String PASSWORD = "Farzana"; // Your MySQL Password

    private static Connection conn;

    // Singleton Method to Get Connection
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection failed!");
        }
        return conn;
    }
}
