import java.sql.*;

public class Create {
    public  static void  createDept(String deptId, String deptName) {
        String query = "INSERT INTO department (deptId, deptName) VALUES(?, ?)";
        try (Connection c = EstablishConnection.getConnection(); PreparedStatement stmt = c.prepareStatement(query);)
        {
            stmt.setString(1, deptId);
            stmt.setString(2, deptName);
            int rows = stmt.executeUpdate();
            System.out.println(rows + " student inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
