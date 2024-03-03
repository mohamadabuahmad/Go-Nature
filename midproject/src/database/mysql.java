package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import logic.ParkVisit;
import ocsf.server.ConnectionToClient;

public class mysql {
	private static final String URL = "jdbc:mysql://localhost/project?serverTimezone=IST&useSSL=false&allowPublicKeyRetrieval=true";

    private static final String USER = "root";
    private static final String PASSWORD = "#Ad112233";


    
    public static void validateDriver() throws ClassNotFoundException {
        // This explicitly loads the JDBC driver class
        Class.forName("com.mysql.cj.jdbc.Driver");
    }
    
    
    
    
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public int insertOrder(ParkVisit order) {
        String query = "INSERT INTO Orders (parkName, timeOfVisit, numberOfVisitors, telephone, email) VALUES (?, ?, ?, ?, ?)";
        int generatedKey = -1;
        
        try (Connection conn = mysql.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, order.getParkName());
            pstmt.setString(2, order.getTimeOfVisit());
            pstmt.setInt(3, order.getNumberOfVisitors());
            pstmt.setString(4, order.getTelephone());
            pstmt.setString(5, order.getEmail());
            
            pstmt.executeUpdate();
            
            // Retrieve the auto-generated key
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKey = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedKey; // Return the generated order number
    }

    public void updateOrder(int orderNumber, String newParkName, String newTelephone) {
    
        String query = "UPDATE Orders SET parkName = ?, telephone = ? WHERE orderNumber = ?";
        
        try (Connection conn = mysql.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, newParkName);
            pstmt.setString(2, newTelephone);
            pstmt.setInt(3, orderNumber);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Order updated successfully.");
            } else {
                System.out.println("No order was updated. Check if the orderNumber exists.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in updateOrder method: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    public List<ParkVisit> fetchAllOrders() {
        List<ParkVisit> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ParkVisit order = new ParkVisit(
                    rs.getInt("orderNumber"),
                    rs.getString("parkName"),
                    rs.getString("timeOfVisit"),
                    rs.getInt("numberOfVisitors"),
                    rs.getString("telephone"),
                    rs.getString("email")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in fetchAllOrders method: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    public int add_To(ParkVisit order) {
        String query = "INSERT INTO Orders (parkName, timeOfVisit, numberOfVisitors, telephone, email) VALUES (?, ?, ?, ?, ?)";
        int generatedKey = -1;

        try (Connection conn = mysql.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, order.getParkName());
            pstmt.setString(2, order.getTimeOfVisit());
            pstmt.setInt(3, order.getNumberOfVisitors());
            pstmt.setString(4, order.getTelephone());
            pstmt.setString(5, order.getEmail());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKey = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in add_To method: " + e.getMessage());
            e.printStackTrace();
        }

        return generatedKey;
    }



    
    // The add_To method seems to be a placeholder. If it's intended for database operations, you'd implement it similar to insertOrder, updateOrder, or getAllOrders.
}
