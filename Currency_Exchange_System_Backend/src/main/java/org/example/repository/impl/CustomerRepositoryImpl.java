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
            throw new RuntimeException("Error in save customer: " + e.getMessage(), e);
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

        // user id , phone
    }
}
