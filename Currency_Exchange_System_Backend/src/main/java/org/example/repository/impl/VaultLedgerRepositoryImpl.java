package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.enums.LedgerReason;
import org.example.model.VaultLedger;
import org.example.repository.interfaces.VaultLedgerRepository;
import org.springframework.stereotype.Repository;

import javax.naming.ldap.PagedResultsControl;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<VaultLedger> findAll() {

        List<VaultLedger> allLedgers = new ArrayList<>();
        String sql = """
                SELECT * FROM vault_ledgers
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                allLedgers.add(mapVaultLedger(resultSet));
            }

            return allLedgers;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all vault ledgers: " + e, e);

        }
    }

    @Override
    public VaultLedger findById(int id) {

        String sql = """
                SELECT * FROM vault_ledgers WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return mapVaultLedger(resultSet);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find vault ledger by id: " + e, e);

        }
    }

    private VaultLedger mapVaultLedger(ResultSet resultSet) throws SQLException {

        VaultLedger vaultLedger = new VaultLedger();
        vaultLedger.setId(resultSet.getInt("id"));
        vaultLedger.setCreatedAt(LocalDateTime.parse(resultSet.getString("created_at")));
        vaultLedger.setPreformedByUserId(resultSet.getInt("performed_by_userId"));
        vaultLedger.setChangeAmount(new BigDecimal(resultSet.getString("change_amount")));
        vaultLedger.setReason(LedgerReason.valueOf(resultSet.getString("reason")));
        vaultLedger.setCurrencyId(resultSet.getInt("currency_id"));

        return vaultLedger;

    }
}
