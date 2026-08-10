package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.model.Customer;
import org.example.repository.interfaces.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public Customer save(Customer customer) {

        String sql = """
                INSERT INTO customers (
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

            statement.setString(1, customer.getFullname());
            statement.setString(2, customer.getNationalId());
            statement.setString(3, customer.getPhoneNumber());
            statement.setInt(4, customer.getUserId().intValue());

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                customer.setId(keys.getLong(1));
            }

            return customer;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save customer: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = """
                SELECT * FROM customers
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                customers.add(mapCustomer(resultSet));
            }

            return customers;


        } catch (SQLException e) {
            throw new RuntimeException("Error in find all customers: " + e.getMessage(), e);
        }
    }

    @Override
    public Customer findById(int customerId) {

        String sql = """
                SELECT * FROM customers WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {
            statement.setInt(1, customerId);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return mapCustomer(resultSet);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find customer by id: " + e.getMessage(), e);
        }
    }

    @Override
    public Customer findByUserId(int userId) {

        String sql = """
                SELECT * FROM customers WHERE user_id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return mapCustomer(resultSet);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find customer by userid" + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByUserId(int userId) {

        String sql = """
                SELECT 1 FROM customers WHERE user_id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error in existing customer by user_id: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsById(int userId) {

        String sql = """
                SELECT 1 FROM customers WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error in existing customer by id: " + e.getMessage(), e);

        }


    }

    @Override
    public boolean existsByPhone(String phone) {

        String sql = """
                SELECT 1 FROM customers WHERE phone_number = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setString(1, phone);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error in existing customer by phone_number: " + e.getMessage(), e);

        }
    }

    @Override
    public Customer update(Customer customer) {

        String sql = """
                UPDATE customers SET
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

            statement.setString(1, customer.getFullname());
            statement.setString(2, customer.getNationalId());
            statement.setString(3, customer.getPhoneNumber());
            statement.setInt(4, customer.getUserId().intValue());
            statement.setInt(5, customer.getId().intValue());

            statement.executeUpdate();
            return customer;

        } catch (SQLException e) {
            throw new RuntimeException("Error in update customer: " + e.getMessage(), e);

        }


    }


    @Override
    public void delete(int customerId) {

        String sql = """
                DELETE FROM customers WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, customerId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error in remove customer: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByNationalId(String nationalId) {

        String sql = """
                SELECT 1 FROM customers WHERE national_id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setString(1, nationalId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error in existing customer by national id: " + e, e);
        }
    }

    @Override
    public List<Customer> searchByName(String name) {

        List<Customer> customers = new ArrayList<>();
        String sql = """
                SELECT * FROM customers WHERE full_name LIKE ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            String searchTerm = "%" + name + "%";
            statement.setString(1, searchTerm);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                customers.add(mapCustomer(resultSet));
            }

            return customers;


        } catch (SQLException e) {
            throw new RuntimeException("Error in search customer by name: " + e, e);
        }

    }

    @Override
    public Customer findByNationalId(String nationalId) {

        String sql = """
                SELECT * FROM customers WHERE national_id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setString(1, nationalId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return mapCustomer(resultSet);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find customer by national id: " + e, e);

        }
    }

    private Customer mapCustomer(ResultSet resultSet) throws SQLException {

        Customer customer = new Customer();
        customer.setFullname(resultSet.getString("full_name"));
        customer.setId(resultSet.getInt("id"));
        customer.setNationalId(resultSet.getString("national_id"));
        customer.setUserId(resultSet.getLong("user_id"));
        customer.setPhoneNumber(resultSet.getString("phone_number"));

        return customer;

    }


}
