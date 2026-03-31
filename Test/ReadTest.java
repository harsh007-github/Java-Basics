import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReadTest {

    private Connection mockConnection;
    private Statement mockStatement;
    private ResultSet mockResultSet;
    private MockedStatic<EstablishConnection> mockedEstablishConnection;

    @BeforeEach
    void setUp() throws SQLException {
        // 1. Create the stunt doubles
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class); // Notice: Statement, not PreparedStatement
        mockResultSet = mock(ResultSet.class); // The fake table of data

        // 2. Chain the mocks together
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // 3. Hijack the static database connection
        mockedEstablishConnection = mockStatic(EstablishConnection.class);
        mockedEstablishConnection.when(EstablishConnection::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    void tearDown() {
        if (mockedEstablishConnection != null) {
            mockedEstablishConnection.close();
        }
    }

    @Test
    @DisplayName("Should retrieve and process all departments from the ResultSet")
    void testGetAllDept_Success() throws SQLException {
        // --- ARRANGE ---
        // 1. Program the ResultSet to have 2 rows of data.
        // next() returns true for row 1, true for row 2, and false to end the while-loop.
        when(mockResultSet.next()).thenReturn(true, true, false);

        // 2. Program what the columns contain for each row
        // The first time getString is called it returns "CS101", the second time "IT02"
        when(mockResultSet.getString("deptId")).thenReturn("CS101", "IT02");
        when(mockResultSet.getString("deptName")).thenReturn("Computer Science", "Information Tech");

        // --- ACT ---
        Read.getAllDept();

        // --- ASSERT / VERIFY ---
        // Verify the exact SQL query was executed
        verify(mockStatement, times(1)).executeQuery("SELECT * FROM department");

        // Verify that the while-loop asked for the ID and Name exactly 2 times
        verify(mockResultSet, times(2)).getString("deptId");
        verify(mockResultSet, times(2)).getString("deptName");

        // Verify resources were closed
        verify(mockResultSet, times(1)).close();
        verify(mockStatement, times(1)).close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    @DisplayName("Should handle database exceptions gracefully")
    void testGetAllDept_HandlesException() throws SQLException {
        // Force the statement to throw an error when executing the query
        when(mockStatement.executeQuery(anyString())).thenThrow(new SQLException("Database offline"));

        // Act: This should trigger your catch block and not crash
        Read.getAllDept();

        // Verify connection was still closed safely
        verify(mockConnection, times(1)).close();
    }
}