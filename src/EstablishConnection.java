import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EstablishConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/college";
    private static final String name = "root";
    private static final String pass = "root";
    public static Connection getConnection()
    {
        try {
            return DriverManager.getConnection(URL, name, pass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
