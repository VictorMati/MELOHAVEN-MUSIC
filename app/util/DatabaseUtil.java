package app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/harmony_vibe_music";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "@Vmn_6887!7886";

    // Establishes a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // Executes a query and returns the result set
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(query);

            // Set query parameters
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            // Execute the query
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
            throw e;
        } finally {
            // Close resources
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    // Executes an update, insert, or delete query
    public static int executeUpdate(String query, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(query);

            // Set query parameters
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            // Execute the query
            return stmt.executeUpdate();
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
            throw e;
        } finally {
            // Close resources
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    // Handles transactions (begin, commit, rollback)
    public static void executeTransaction(Transaction transaction) throws SQLException {
        Connection conn = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Execute transaction
            transaction.execute(conn);

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback transaction
            }
            e.printStackTrace();
            throw e;
        } finally {
            // Close connection
            if (conn != null) {
                conn.setAutoCommit(true); // Reset auto-commit
                conn.close();
            }
        }
    }

    // Functional interface for transactions
    public interface Transaction {
        void execute(Connection conn) throws SQLException;
    }
}

