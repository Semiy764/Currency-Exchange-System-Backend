package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.enums.UserRole;
import org.example.model.User;
import org.example.repository.interfaces.UserRepsitory;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepsitory {

    @Override
    public User save(User user) {

        String sql = """
                INSERT INTO users (
                username,
                password_hash,
                role,
                is_active)
                VALUES(?, ?, ?, ?)
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getRole().name());
            statement.setInt(4, user.isActive() ? 1 : 0);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }

            return user;

        } catch(SQLException e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }

    @Override
    public User findById(int userId) {
        String sql = """
                SELECT * FROM users WHERE id = ?
                """;


        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return mapUser(resultSet);
            }

            return null;


        } catch (SQLException e) {
            throw new RuntimeException("Error in find user by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> findAll() {

        List<User> allUsers = new ArrayList<>();
        String sql = """
                SELECT * FROM users
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                allUsers.add(mapUser(resultSet));
            }
            return allUsers;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all users: " + e.getMessage(), e);
        }
    }

    @Override
    public User findByUsername(String username) {

        String sql = """
                SELECT * FROM users WHERE username = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return mapUser(resultSet);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find user by username: " + e.getMessage(), e);

        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = """
                SELECT 1 FROM users WHERE username = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error in determine existing user by username: " + e.getMessage(), e);

        }
    }

    private User mapUser(ResultSet res) throws SQLException {

        User user = new User();
        user.setId(res.getLong("id"));
        user.setUsername(res.getString("username"));
        user.setPasswordHash(res.getString("password_hash"));
        user.setRole(UserRole.valueOf(res.getString("role")));
        user.setActive(res.getInt("is_active") == 1);

        return user;
    }

    @Override
    public boolean existsById(int userId) {

        String sql = """
                SELECT 1 FROM users WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error in determine existing user by id: " + e.getMessage(), e);


        }
    }
}
