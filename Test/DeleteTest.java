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

class DeleteTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private MockedStatic<EstablishConnection> mockedEstablishConnection;

    @BeforeEach
    void setUp() throws SQLException {
        // 1. Initialize mocks
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        // 2. Define behavior: Connection should return our mock statement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // 3. Mock the static method EstablishConnection.getConnection()
        mockedEstablishConnection = mockStatic(EstablishConnection.class);
        mockedEstablishConnection.when(EstablishConnection::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    void tearDown() {
        // Always close static mocks to prevent memory leaks and test interference
        if (mockedEstablishConnection != null) {
            mockedEstablishConnection.close();
        }
    }

    @Test
    @DisplayName("Should successfully execute DELETE statement with correct ID")
    void testDeleteDept_Success() throws SQLException {
        // Arrange
        String targetId = "CS101";
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate 1 row deleted

        // Act
        Delete.deleteDept(targetId);

        // Assert/Verify
        // Verify the code called setString(1, "CS101")
        verify(mockPreparedStatement, times(1)).setString(1, targetId);

        // Verify the delete command was actually executed
        verify(mockPreparedStatement, times(1)).executeUpdate();

        // Verify try-with-resources closed the objects
        verify(mockPreparedStatement, times(1)).close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    @DisplayName("Should handle SQLException gracefully")
    void testDeleteDept_HandlesError() throws SQLException {
        // Arrange
        String targetId = "MATH02";
        // Simulate a database failure (like a foreign key constraint violation)
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Foreign Key Violation"));

        // Act
        // The method catches the error internally, so the test should not crash
        Delete.deleteDept(targetId);

        // Verify it still tried to execute and close
        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockConnection, times(1)).close();
    }
}