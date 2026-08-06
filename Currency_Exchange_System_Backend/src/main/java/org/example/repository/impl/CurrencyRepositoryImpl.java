package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.model.Currency;
import org.example.repository.interfaces.CurrencyRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.tokens.ScalarToken;

import java.sql.*;
import java.time.Period;
import java.util.List;


@Repository
public class CurrencyRepositoryImpl implements CurrencyRepository {

    @Override
    public Currency save(Currency currency) {

        String sql = """
                INSERT INTO currencies(
                code,
                name, 
                symbol)
                VALUES(?, ?, ?)
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSymbol());

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                currency.setId(keys.getLong(1));
            }

            return currency;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save currency: " + e, e);
        }
    }

//    @Override
//    public List<Currency> findAll() {
//
//        String sql = """
//                SELECT * FROM currencies
//                """;
//
//        try(
//                Connection connection = DatabaseManager.getConnection();
//                PreparedStatement statement = connection.prepareStatement(sql);
//                ) {
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Error in save currency: " + e, e);
//
//        }
//    }
}
