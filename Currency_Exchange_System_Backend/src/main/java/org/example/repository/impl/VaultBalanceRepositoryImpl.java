package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.springframework.stereotype.Repository;

import javax.naming.ldap.PagedResultsControl;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VaultBalanceRepositoryImpl implements VaultBalanceRepository {
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

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ) {

            statement.setInt(1, vaultBalance.getCurrencyId().intValue());
            statement.setString(2, vaultBalance.getBalance().toString());
            statement.setString(3, vaultBalance.getLastUpdated().toString());

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                vaultBalance.setId(keys.getLong(1));
            }

            return vaultBalance;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save vault balance: " + e, e);
        }
    }

    @Override
    public List<VaultBalance> findAll() {

        List<VaultBalance> allBalances = new ArrayList<>();
        String sql = """
                SELECT * FROM vault_balances
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                allBalances.add(mapBalances(resultSet));
            }
            return allBalances;


        } catch (SQLException e) {
            throw new RuntimeException("Error in find all balances: " + e, e);
        }
    }

    @Override
    public boolean existsByCurrencyId(int currencyId) {

        String sql = """
                SELECT 1 FROM vault_balances WHERE
                currency_id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, currencyId);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error in exist balance by currency id: " + e, e);
        }
    }

    @Override
    public VaultBalance findByCurrencyId(int currencyId) {
        String sql = """
                SELECT * FROM vault_balances WHERE currency_id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, currencyId);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return mapBalances(resultSet);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find balance by currency id: " + e, e);

        }

    }

    @Override
    public void adjustBalance(int currencyId, BigDecimal amount) {

        String sql = """
                UPDATE vault_balances SET balance = ?
                WHERE currency_id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setString(1, amount.toString());
            statement.setInt(2, currencyId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error in adjust balance: " + e, e);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = """
                DELETE FROM vault_balances WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error in delete vault balance: " + e, e);
        }
    }

    @Override
    public VaultBalance findById(int id) {

        String sql = """
                SELECT * FROM vault_balances WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return mapBalances(resultSet);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find balance by id: " + e, e);
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
