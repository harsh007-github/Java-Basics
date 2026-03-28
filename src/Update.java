import java.sql.Connection;
import java.sql.PreparedStatement;

public class Update {
    public  static void updateDept(String deptId, String deptName) {
        String query = "UPDATE department SET deptName = ? WHERE  deptId = ?";
        try (
                Connection conn = EstablishConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ){
            stmt.setString(1, deptName);
            stmt.setString(2, deptId);
            int rows = stmt.executeUpdate();
            System.out.println(rows + " student updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
