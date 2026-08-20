package org.example.repository.impl;

import org.example.exception.ResourceNotFoundException;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VaultBalanceRepositoryImpl implements VaultBalanceRepository {

    @Autowired
    private DataSource dataSource;
    // vault balance mojidiye har currency ro moshakhas mikone!!!!
    @Override
    public VaultBalance save(VaultBalance vaultBalance) {

        String sql = """
                INSERT INTO vault_balances (
                currency_id,
                balance,
                lastUpdated)
                VALUES (?, ?, ?)
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, vaultBalance.getCurrencyId().intValue());
            statement.setString(2, vaultBalance.getBalance().toString());
            statement.setString(3, vaultBalance.getLastUpdated().toString());

            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if(keys.next()) {
                    vaultBalance.setId(keys.getLong(1));
                }
            }

            return vaultBalance;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save vault balance: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<VaultBalance> findAll() {

        List<VaultBalance> allBalances = new ArrayList<>();
        String sql = """
                SELECT * FROM vault_balances
                """;
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                ) {

            while(resultSet.next()) {
                allBalances.add(mapBalances(resultSet));
            }
            return allBalances;


        } catch (SQLException e) {
            throw new RuntimeException("Error in find all balances: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public boolean existsByCurrencyId(int currencyId) {

        String sql = """
                SELECT 1 FROM vault_balances WHERE
                currency_id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currencyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }


        } catch (SQLException e) {
            throw new RuntimeException("Error in exist balance by currency id: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public VaultBalance findByCurrencyId(int currencyId) {
        String sql = """
                SELECT * FROM vault_balances WHERE currency_id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currencyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapBalances(resultSet);
                }
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException("Error in find balance by currency id: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    @Override
    public void adjustBalance(int currencyId, BigDecimal amount) {

        String sql = """
                UPDATE vault_balances SET
                balance = balance + ?,
                lastUpdated = ?
                WHERE currency_id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, amount);
            statement.setString(2, LocalDateTime.now().toString());
            statement.setInt(3, currencyId);

            int rows = statement.executeUpdate();
            if(rows == 0) {
                throw new ResourceNotFoundException("Currency not found in vault ledger");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in adjust balance: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = """
                DELETE FROM vault_balances WHERE id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new ResourceNotFoundException("Vault balance not found with ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in delete vault balance: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public VaultBalance findById(int id) {

        String sql = """
                SELECT * FROM vault_balances WHERE id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapBalances(resultSet);
                }
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException("Error in find balance by id: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<VaultBalance> findByBalanceLessThan(BigDecimal threshold) {

        List<VaultBalance> balances = new ArrayList<>();
        String sql = """
                SELECT * FROM vault_balances WHERE
                CAST (balance AS REAL) <= ?
                ORDER BY CAST (balance AS REAL) DESC
                """;
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, threshold);

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    balances.add(mapBalances(resultSet));
                }
            }

            return balances;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find by balance less than: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    private VaultBalance mapBalances(ResultSet resultSet) throws SQLException {

        VaultBalance vaultBalance = new VaultBalance();

        vaultBalance.setId(resultSet.getInt("id"));
        vaultBalance.setCurrencyId(resultSet.getInt("currency_id"));
        vaultBalance.setBalance(new BigDecimal(resultSet.getString("balance")));
        vaultBalance.setLastUpdated(LocalDateTime.parse(resultSet.getString("lastUpdated")));

        return vaultBalance;
    }
 }
