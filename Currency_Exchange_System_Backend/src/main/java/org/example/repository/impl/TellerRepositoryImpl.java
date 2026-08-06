package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.model.Teller;
import org.example.repository.interfaces.TellerRepository;
import org.springframework.stereotype.Repository;

import javax.naming.ldap.PagedResultsControl;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TellerRepositoryImpl implements TellerRepository {

    @Override
    public Teller save(Teller teller) {

        String sql = """
                INSERT INTO tellers(
                full_name,
                national_id,
                phone_number,
                user_id)
                VALUES(?, ?, ?, ?)
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ) {

            statement.setString(1, teller.getFullname());
            statement.setString(2, teller.getNationalId());
            statement.setString(3, teller.getPhoneNumber());
            statement.setInt(4, teller.getUserId().intValue());

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                teller.setId(keys.getLong(1));
            }
            return teller;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save teller: " + e, e);
        }
    }

    @Override
    public List<Teller> findAll() {

        List<Teller> allTellers = new ArrayList<>();
        String sql = """
                SELECT * FROM tellers
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                allTellers.add(mapTeller(resultSet));
            }
            return allTellers;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all tellers: " + e, e);
        }
    }

    @Override
    public Teller findById(int tellerId) {

        String sql = """
                SELECT * FROM tellers WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {
            statement.setInt(1, tellerId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return mapTeller(resultSet);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find teller by teller id: " + e, e);
        }
    }

    @Override
    public Teller findByUserId(int tellerId) {
        String sql = """
                SELECT * FROM tellers WHERE user_id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, tellerId);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return mapTeller(resultSet);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find teller by user id: " + e, e);
        }
    }

    @Override
    public boolean existsByUserId(int userId) {

        String sql = """
                SELECT 1 FROM tellers WHERE user_id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error in existing teller by user id: " + e, e);
        }
    }

    @Override
    public boolean existsById(int tellerId) {

        String sql = """
                SELECT 1 FROM tellers WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, tellerId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error in existing teller by id: " + e, e);

        }
    }

    @Override
    public boolean existsByPhone(String phone) {

        String sql = """
                SELECT 1 FROM tellers WHERE phone_number = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setString(1, phone);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error in existing teller by phone_number: " + e, e);

        }


    }

    @Override
    public void delete(int tellerId) {

        String sql = """
                DELETE FROM tellers WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, tellerId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error in delete teller: " + e, e);

        }
    }

    @Override
    public void deleteByUserId(int userId) {

        String sql = """
                DELETE FROM tellers WHERE user_id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error in delete teller: " + e, e);

        }
    }

    @Override
    public Teller update(Teller teller) {

        String sql = """
                UPDATE tellers SET
                full_name = ?,
                national_id = ?,
                phone_number = ?,
                user_id = ?
                WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setString(1, teller.getFullname());
            statement.setString(2, teller.getNationalId());
            statement.setString(3, teller.getPhoneNumber());
            statement.setInt(4, teller.getUserId().intValue());
            statement.setInt(5, teller.getId().intValue());

            statement.executeUpdate();
            return teller;

        } catch (SQLException e) {
            throw new RuntimeException("Error in delete teller: " + e, e);

        }
    }

    private Teller mapTeller(ResultSet resultSet) throws SQLException {

        Teller teller = new Teller();
        teller.setId(resultSet.getInt("id"));
        teller.setUserId(resultSet.getLong("user_id"));
        teller.setFullname(resultSet.getString("full_name"));
        teller.setNationalId(resultSet.getString("national_id"));
        teller.setPhoneNumber(resultSet.getString("phone_number"));

        return teller;
    }
}
