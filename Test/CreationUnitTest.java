import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CreationUnitTest {

    // These variables will hold our "dummy" database objects
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private MockedStatic<EstablishConnection> mockedEstablishConnection;

    @BeforeEach
    void setUp() throws SQLException {
        System.out.println("--- @BeforeEach: Setting up the mocks ---");

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        System.out.println("1. Created dummy Connection and PreparedStatement objects.");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        System.out.println("2. Instructed dummy Connection to return dummy PreparedStatement when asked.");

        mockedEstablishConnection = mockStatic(EstablishConnection.class);
        mockedEstablishConnection.when(EstablishConnection::getConnection).thenReturn(mockConnection);
        System.out.println("3. Hijacked EstablishConnection.getConnection() to return our dummy Connection.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("--- @AfterEach: Cleaning up ---");
        if (mockedEstablishConnection != null) {
            mockedEstablishConnection.close();
            System.out.println("Closed the static mock to prevent interference with other tests.\n");
        }
    }

    @Test
    void createDept_Success() throws SQLException {
        System.out.println("--- @Test: createDept_Success() START ---");

        // 1. ARRANGE (Set up test conditions)
        String deptId = "IT01";
        String deptName = "Information Technology";
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        System.out.println("Test Setup: Told dummy PreparedStatement to return '1' when executeUpdate() is called.");

        // 2. ACT (Call the real method)
        System.out.println("Executing actual Create.createDept() method...");
        Create.createDept(deptId, deptName);
        System.out.println("Finished executing actual method.");

        // 3. ASSERT / VERIFY (Check what happened inside the method)
        System.out.println("Verifying interactions with the mock objects...");

        verify(mockPreparedStatement, times(1)).setString(1, deptId);
        System.out.println("Verified: setString(1, 'IT01') was called exactly once.");

        verify(mockPreparedStatement, times(1)).setString(2, deptName);
        System.out.println("Verified: setString(2, 'Information Technology') was called exactly once.");

        verify(mockPreparedStatement, times(1)).executeUpdate();
        System.out.println("Verified: executeUpdate() was called exactly once.");

        verify(mockPreparedStatement, times(1)).close();
        verify(mockConnection, times(1)).close();
        System.out.println("Verified: Both the statement and connection were closed safely.");

        System.out.println("--- @Test: createDept_Success() END ---");
    }
}