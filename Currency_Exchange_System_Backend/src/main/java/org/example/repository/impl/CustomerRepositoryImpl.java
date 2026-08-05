package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.model.Customer;
import org.example.repository.interfaces.CustomerRepository;

import java.sql.*;

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
                customer.setId(keys.getLong("id"));
            }

            return customer;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save customer: " + e.getMessage(), e);
        }
    }
}
