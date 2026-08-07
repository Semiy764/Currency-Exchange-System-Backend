package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.springframework.stereotype.Repository;

import javax.naming.ldap.PagedResultsControl;
import java.sql.*;

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
}
