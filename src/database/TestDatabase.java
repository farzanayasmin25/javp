package database;

import java.sql.Connection;

public class TestDatabase {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Connected to MySQL successfully!");
        } else {
            System.out.println("Connection failed.");
        }
    }
}
