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

    
}
