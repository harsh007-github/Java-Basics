import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UpdateTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private MockedStatic<EstablishConnection> mockedEstablishConnection;

    @BeforeEach
    void setUp() throws SQLException {
        // 1. Create the stunt doubles
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        // 2. Program the connection to return the fake statement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // 3. Hijack the static database connection
        mockedEstablishConnection = mockStatic(EstablishConnection.class);
        mockedEstablishConnection.when(EstablishConnection::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    void tearDown() {
        // Clean up the static mock so it doesn't break other tests
        if (mockedEstablishConnection != null) {
            mockedEstablishConnection.close();
        }
    }

    @Test
    @DisplayName("Should update department with correct parameter order")
    void testUpdateDept_Success() throws SQLException {
        // Arrange
        String targetId = "CS101";
        String newName = "Computer Science and Engineering";
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        Update.updateDept(targetId, newName);

        // Assert/Verify
        // CRITICAL: We verify that the Name went to index 1, and the ID went to index 2!
        verify(mockPreparedStatement, times(1)).setString(1, newName);
        verify(mockPreparedStatement, times(1)).setString(2, targetId);

        // Verify it executed
        verify(mockPreparedStatement, times(1)).executeUpdate();

        // Verify resources were closed automatically by try-with-resources
        verify(mockPreparedStatement, times(1)).close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    @DisplayName("Should handle database exceptions gracefully")
    void testUpdateDept_HandlesException() throws SQLException {
        // Arrange
        String targetId = "IT02";
        String newName = "Information Tech";

        // Force the mock to throw an error to test the catch block
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Table locked"));

        // Act
        // The catch(Exception e) block in your code should prevent a crash
        Update.updateDept(targetId, newName);

        // Assert
        // Verify that even during a crash, the connection was closed safely
        verify(mockConnection, times(1)).close();
    }
}