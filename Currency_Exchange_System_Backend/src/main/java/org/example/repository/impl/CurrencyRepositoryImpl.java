package org.example.repository.impl;
import org.example.exception.EntityNotFoundException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Currency;
import org.example.repository.interfaces.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// agar query hich ? nadare resultset ro tooye try avali biyar vali agar ? dasht bayad yek try joda barash benevisi!!
// harja dar query update ya delete bood bayad int rows begiri!!! va dige nemikhad dar try bebandish!!!
@Repository
public class CurrencyRepositoryImpl implements CurrencyRepository {

    @Autowired
    private DataSource dataSource;

    @Override
    public Currency save(Currency currency) {

        String sql = """
                INSERT INTO currencies(
                code,
                name, 
                symbol)
                VALUES(?, ?, ?)
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSymbol());

            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if(keys.next()) {
                    currency.setId(keys.getLong(1));
                }
            }

            return currency;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save currency: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<Currency> findAll() {

        List<Currency> allCurrencies = new ArrayList<>();
        String sql = """
                SELECT * FROM currencies
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

        ) {

            while(resultSet.next()) {
                allCurrencies.add(mapCurrency(resultSet));
            }

            return allCurrencies;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all currencies: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public Currency findById(int currencyId) {

        String sql = """
                SELECT * FROM currencies WHERE id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, currencyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapCurrency(resultSet);
                }
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException("Error in find currency by id: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public Currency findBySymbol(String symbol) {

        String sql = """
                SELECT * FROM currencies WHERE symbol = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, symbol);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapCurrency(resultSet);
                }
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException("Error in find currency by symbol: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public Currency findByName(String name) {

        String sql = """
                SELECT * FROM currencies WHERE name = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapCurrency(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in find currency by name: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public Currency findByCode(String code) {
        String sql = """
                SELECT * FROM currencies WHERE code = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapCurrency(resultSet);
                }
                return null;
            }




        } catch (SQLException e) {
            throw new RuntimeException("Error in find currency by code: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public void delete(int currencyId) {

        String sql = """
                DELETE FROM currencies WHERE id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currencyId);
            int rows = statement.executeUpdate();
            if(rows == 0) {
                throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
            }


        } catch (SQLException e) {
            throw new RuntimeException("Error in delete currency: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public boolean existsById(int currencyId) {

        String sql = """
                SELECT 1 FROM currencies WHERE id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currencyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in existing currency by id: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public boolean existsByName(String name) {

        String sql = """
                SELECT 1 FROM currencies WHERE name = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in existing currency by name: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public boolean existsByCode(String code) {

        String sql = """
                SELECT 1 FROM currencies WHERE code = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setString(1, code);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in existing currency by code: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public Currency update(Currency currency) {

        String sql = """
                UPDATE currencies SET
                code = ?,
                name = ?,
                symbol = ?
                WHERE id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSymbol());
            statement.setInt(4, currency.getId().intValue());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new ResourceNotFoundException("Currency not found with ID: " + currency.getId());
            }

            return currency;

        } catch (SQLException e) {
            throw new RuntimeException("Error in update currency: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }


    @Override
    public List<Currency> findAllActiveCurrencies() {

        List<Currency> foundCurrencies = new ArrayList<>();
        String sql = """
                SELECT * FROM currencies WHERE is_active = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, 1);

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    foundCurrencies.add(mapCurrency(resultSet));
                }
            }

            return foundCurrencies;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all active currencies: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public void deactivateCurrency(int id) {

        String sql = """
                UPDATE currencies SET
                is_active = 0 WHERE id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int rows = statement.executeUpdate();

            if(rows == 0) {
                throw new EntityNotFoundException("Currency not found or already inactive with ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in deactivate currency: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public void activateCurrency(int id) {

        String sql = """
                UPDATE currencies SET 
                is_active = 1
                WHERE id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int rows = statement.executeUpdate();

            if(rows == 0) {
                throw new EntityNotFoundException("Currency not found or already active with ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in activate currency: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public boolean isActive(int currencyId) {

        String sql = """
                SELECT is_active FROM currencies WHERE id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currencyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getBoolean("is_active");
                } else {
                    throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in currency is Active: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    private Currency mapCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setId(resultSet.getLong("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setName(resultSet.getString("name"));
        currency.setSymbol(resultSet.getString("symbol"));
        currency.setActive(resultSet.getInt("is_active") == 1);
        return currency;

    }
}
