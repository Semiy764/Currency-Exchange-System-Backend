package Repository;

import org.example.database.DatabaseManager;
import org.example.enums.UserRole;
import org.example.model.User;
import org.example.repository.impl.UserRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserRepositoryImpl}.
 * <p>
 * DatabaseManager.getConnection() is a static call to a real SQLite file,
 * so it is mocked via Mockito's mockStatic() for every test. This keeps
 * these as true unit tests: no real database is touched, and each test
 * only verifies the SQL that is executed and how ResultSet rows are
 * mapped back into User objects.
 */
@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    private UserRepositoryImpl userRepository;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement statement;

    @Mock
    private ResultSet resultSet;

    private MockedStatic<DatabaseManager> databaseManagerMock;

    @BeforeEach
    void setUp() throws SQLException {
        userRepository = new UserRepositoryImpl();

        databaseManagerMock = mockStatic(DatabaseManager.class);
        databaseManagerMock.when(DatabaseManager::getConnection).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        databaseManagerMock.close();
    }

    private User buildUser() {
        User user = new User();
        user.setUsername("erfan");
        user.setPasswordHash("hashed-password");
        user.setRole(UserRole.CUSTOMER);
        user.setActive(true);
        return user;
    }

    @Nested
    @DisplayName("save()")
    class Save {

        @Test
        @DisplayName("inserts the user and sets the generated id on success")
        void save_success_setsGeneratedId() throws SQLException {
            User user = buildUser();

            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(statement);
            when(statement.executeUpdate()).thenReturn(1);
            when(statement.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getLong(1)).thenReturn(42L);

            User saved = userRepository.save(user);

            assertEquals(42L, saved.getId());
            verify(statement).setString(1, "erfan");
            verify(statement).setString(2, "hashed-password");
            verify(statement).setString(3, UserRole.CUSTOMER.name());
            verify(statement).setInt(4, 1);
            verify(statement).executeUpdate();
        }

        @Test
        @DisplayName("wraps SQLException in a RuntimeException")
        void save_sqlException_throwsRuntimeException() throws SQLException {
            User user = buildUser();

            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(statement);
            when(statement.executeUpdate()).thenThrow(new SQLException("insert failed"));

            RuntimeException ex = assertThrows(RuntimeException.class, () -> userRepository.save(user));
            assertTrue(ex.getMessage().contains("Error saving user"));
            assertInstanceOf(SQLException.class, ex.getCause());
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("returns the mapped user when a row is found")
        void findById_found_returnsUser() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getLong("id")).thenReturn(1L);
            when(resultSet.getString("username")).thenReturn("erfan");
            when(resultSet.getString("password_hash")).thenReturn("hashed-password");
            when(resultSet.getString("role")).thenReturn(UserRole.CUSTOMER.name());
            when(resultSet.getInt("is_active")).thenReturn(1);

            User found = userRepository.findById(1);

            assertNotNull(found);
            assertEquals("erfan", found.getUsername());
            assertEquals(UserRole.CUSTOMER, found.getRole());
            assertTrue(found.isActive());
            verify(statement).setInt(1, 1);
        }

        @Test
        @DisplayName("returns null when no row is found")
        void findById_notFound_returnsNull() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            assertNull(userRepository.findById(999));
        }

        @Test
        @DisplayName("wraps SQLException in a RuntimeException")
        void findById_sqlException_throwsRuntimeException() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenThrow(new SQLException("query failed"));

            assertThrows(RuntimeException.class, () -> userRepository.findById(1));
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("maps every row into a User list")
        void findAll_returnsAllMappedUsers() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, false);
            when(resultSet.getLong("id")).thenReturn(1L, 2L);
            when(resultSet.getString("username")).thenReturn("erfan", "sara");
            when(resultSet.getString("password_hash")).thenReturn("hash1", "hash2");
            when(resultSet.getString("role")).thenReturn(
                    UserRole.CUSTOMER.name(), UserRole.ADMIN.name());
            when(resultSet.getInt("is_active")).thenReturn(1, 0);

            List<User> users = userRepository.findAll();

            assertEquals(2, users.size());
            assertEquals("erfan", users.get(0).getUsername());
            assertEquals("sara", users.get(1).getUsername());
            assertFalse(users.get(1).isActive());
        }

        @Test
        @DisplayName("returns an empty list when there are no rows")
        void findAll_noRows_returnsEmptyList() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            assertTrue(userRepository.findAll().isEmpty());
        }
    }

    @Nested
    @DisplayName("findByUsername()")
    class FindByUsername {

        @Test
        @DisplayName("returns the mapped user when found")
        void findByUsername_found_returnsUser() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getLong("id")).thenReturn(1L);
            when(resultSet.getString("username")).thenReturn("erfan");
            when(resultSet.getString("password_hash")).thenReturn("hashed-password");
            when(resultSet.getString("role")).thenReturn(UserRole.CUSTOMER.name());
            when(resultSet.getInt("is_active")).thenReturn(1);

            User found = userRepository.findByUsername("erfan");

            assertNotNull(found);
            verify(statement).setString(1, "erfan");
        }

        @Test
        @DisplayName("returns null when not found")
        void findByUsername_notFound_returnsNull() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            assertNull(userRepository.findByUsername("ghost"));
        }
    }

    @Nested
    @DisplayName("existsByUsername() / existsById()")
    class Exists {

        @Test
        @DisplayName("existsByUsername returns true when a row is found")
        void existsByUsername_true() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);

            assertTrue(userRepository.existsByUsername("erfan"));
        }

        @Test
        @DisplayName("existsByUsername returns false when no row is found")
        void existsByUsername_false() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            assertFalse(userRepository.existsByUsername("ghost"));
        }

        @Test
        @DisplayName("existsById returns true when a row is found")
        void existsById_true() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);

            assertTrue(userRepository.existsById(1));
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {

        @Test
        @DisplayName("executes a delete for the given id")
        void delete_executesUpdate() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);

            userRepository.delete(1);

            verify(statement).setInt(1, 1);
            verify(statement).executeUpdate();
        }

        @Test
        @DisplayName("wraps SQLException in a RuntimeException")
        void delete_sqlException_throwsRuntimeException() throws SQLException {
            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeUpdate()).thenThrow(new SQLException("delete failed"));

            assertThrows(RuntimeException.class, () -> userRepository.delete(1));
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("updates username, role, is_active and returns the user")
        void update_success_returnsUser() throws SQLException {
            User user = buildUser();
            user.setId(5L);
            user.setRole(UserRole.ADMIN);

            when(connection.prepareStatement(anyString())).thenReturn(statement);

            User updated = userRepository.update(user);

            assertSame(user, updated);
            verify(statement).setString(1, "erfan");
            verify(statement).setString(2, UserRole.ADMIN.name());
            verify(statement).setInt(3, 1);
            verify(statement).setInt(4, 5);
            verify(statement).executeUpdate();
        }

        @Test
        @DisplayName("wraps SQLException in a RuntimeException")
        void update_sqlException_throwsRuntimeException() throws SQLException {
            User user = buildUser();
            user.setId(5L);

            when(connection.prepareStatement(anyString())).thenReturn(statement);
            when(statement.executeUpdate()).thenThrow(new SQLException("update failed"));

            assertThrows(RuntimeException.class, () -> userRepository.update(user));
        }
    }
}