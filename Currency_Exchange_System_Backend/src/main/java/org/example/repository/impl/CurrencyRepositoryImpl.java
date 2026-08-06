package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.model.Currency;
import org.example.repository.interfaces.CurrencyRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.tokens.ScalarToken;

import java.sql.*;
import java.time.Period;
import java.util.ArrayList;
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

    @Override
    public List<Currency> findAll() {

        List<Currency> allCurrencies = new ArrayList<>();
        String sql = """
                SELECT * FROM currencies
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                allCurrencies.add(mapCurrency(resultSet));
            }

            return allCurrencies;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all currencies: " + e, e);

        }
    }

    @Override
    public Currency findById(int currencyId) {

        String sql = """
                SELECT * FROM currencies WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {
            statement.setInt(1, currencyId);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return mapCurrency(resultSet);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find currency by id: " + e, e);
        }
    }

    @Override
    public Currency findBySymbol(String symbol) {

        String sql = """
                SELECT * FROM currencies WHERE symbol = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setString(1, symbol);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return mapCurrency(resultSet);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find currency by symbol: " + e, e);

        }
    }

    @Override
    public Currency findByName(String name) {

        String sql = """
                SELECT * FROM currencies WHERE name = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return mapCurrency(resultSet);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find currency by name: " + e, e);
        }
    }

    private Currency mapCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setId(resultSet.getLong("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setName(resultSet.getString("name"));
        currency.setSymbol(resultSet.getString("symbol"));

        return currency;

    }
}
