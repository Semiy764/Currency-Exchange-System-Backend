package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.model.VaultLedger;
import org.example.repository.interfaces.VaultLedgerRepository;
import org.springframework.stereotype.Repository;

import javax.naming.ldap.PagedResultsControl;
import java.sql.*;

@Repository
public class VaultLedgerRepositoryImpl implements VaultLedgerRepository {

    @Override
    public VaultLedger save(VaultLedger vaultLedger) {

        String sql = """
                INSERT INTO vault_ledgers (
                currency_id,
                change_amount,
                reason,
                created_at,
                performed_by_userId)
                VALUES(?, ?, ?, ?, ?)
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ) {

            statement.setInt(1, vaultLedger.getCurrencyId().intValue());
            statement.setInt(2, vaultLedger.getChangeAmount().intValue());
            statement.setString(3, vaultLedger.getReason().name());
            statement.setString(4, vaultLedger.getCreatedAt().toString());
            statement.setInt(5, vaultLedger.getPreformedByUserId().intValue());

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                vaultLedger.setId(keys.getLong(1));
            }

            return vaultLedger;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save vault ledger: " + e, e);
        }
    }
}
