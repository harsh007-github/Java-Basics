import java.sql.*;
public  class Main{
    static void main() {
        Connection connection = null;
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/college", "root", "root"
            );
            // Create statement
            Statement statement = connection.createStatement();
            // Execute query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM department");

            // Process results
            while (resultSet.next()) {
                String id = resultSet.getString("deptId").trim();
                String name = resultSet.getString("deptName").trim();
                System.out.println("Dept Id: " + id + " | Name: " + name);
            }
            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

//"C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
//"-javaagent:C:\Users\khand\AppData\Local\Programs\IntelliJ IDEA 2025.3.3\lib\idea_rt.jar=54609"
//-Dfile.encoding=UTF-8
//-Dsun.stdout.encoding=UTF-8
//-Dsun.stderr.encoding=UTF-8
//-classpath "E:\Harsh\Java\Java Basics\out\production\Java Basics"
//Main
