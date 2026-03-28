import java.sql.Connection;
import java.sql.PreparedStatement;

public class Delete {
    public  static void deleteDept(String deptId) {
        String query = "DELETE FROM department WHERE deptId = ?";
        try(
                Connection conn = EstablishConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        )
        {
            stmt.setString(1, deptId);
            int rows = stmt.executeUpdate();
            System.out.println(rows + " student updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
