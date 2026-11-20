package src;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        Connection con = null;

        try {
            String url = "jdbc:mysql://localhost:3306/ExamInsightDB";
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (user == null || password == null) {
                throw new Exception("Database credentials not configured!");
            }

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(url, user, password);

            System.out.println("Database Connected Successfully!");

        } catch (Exception e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }

        return con;
    }
}
