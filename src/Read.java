import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Read {
    public static void getAllDept()
    {
        String query = "SELECT * FROM department";
        try (Connection conn = EstablishConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            while (resultSet.next()) {
                String id = resultSet.getString("deptId").trim();
                String name = resultSet.getString("deptName").trim();
                System.out.println("Dept Id: " + id + " | Name: " + name);
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
