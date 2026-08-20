package org.example.repository.impl;

import org.example.enums.LedgerReason;
import org.example.model.VaultLedger;
import org.example.repository.interfaces.VaultLedgerRepository;
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
public class VaultLedgerRepositoryImpl implements VaultLedgerRepository {

    @Autowired
    private DataSource dataSource;

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

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, vaultLedger.getCurrencyId().intValue());
            statement.setInt(2, vaultLedger.getChangeAmount().intValue());
            statement.setString(3, vaultLedger.getReason().name());
            statement.setString(4, vaultLedger.getCreatedAt().toString());
            statement.setInt(5, vaultLedger.getPreformedByUserId().intValue());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if(keys.next()) {
                    vaultLedger.setId(keys.getLong(1));
                }
            }

            return vaultLedger;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save vault ledger: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    @Override
    public List<VaultLedger> findAll() {

        List<VaultLedger> allLedgers = new ArrayList<>();
        String sql = """
                SELECT * FROM vault_ledgers
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                ) {

            while(resultSet.next()) {
                allLedgers.add(mapVaultLedger(resultSet));
            }
            return allLedgers;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all vault ledgers: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public VaultLedger findById(int id) {

        String sql = """
                SELECT * FROM vault_ledgers WHERE id = ?
                """;


        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapVaultLedger(resultSet);
                }
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException("Error in find vault ledger by id: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<VaultLedger> findByCurrencyIdOrderByCreatedAtDesc(int currencyId) {

        List<VaultLedger> ledgers = new ArrayList<>();
        String sql = """
                SELECT * FROM vault_ledgers WHERE currency_id = ?
                ORDER BY created_at DESC
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currencyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ledgers.add(mapVaultLedger(resultSet));
                }
            }

            return ledgers;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find vault ledgers by currency id: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<VaultLedger> findByCurrencyIdAndCreatedAtBetween(int currencyId,
                                                                 LocalDateTime start,
                                                                 LocalDateTime end) {

        List<VaultLedger> ledgers = new ArrayList<>();
        String sql = """
                SELECT * FROM vault_ledgers WHERE 
                created_at BETWEEN ? AND ? AND
                currency_id = ? ORDER BY created_at
                DESC
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, start.toString());
            statement.setString(2, end.toString());
            statement.setInt(3, currencyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    ledgers.add(mapVaultLedger(resultSet));
                }
            }

            return ledgers;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find by currency id and created at between: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<VaultLedger> findByPerformedByUserId(int userId) {

        List<VaultLedger> ledgers = new ArrayList<>();
        String sql = """
                SELECT * FROM vault_ledgers WHERE performed_by_userId = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ledgers.add(mapVaultLedger(resultSet));
                }
            }

            return ledgers;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find by performed by user id: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }


    }

    @Override
    public List<VaultLedger> findTopNByOrderByCreatedAtDesc(int limit) {

        List<VaultLedger> ledgers = new ArrayList<>();
        String sql = """
                SELECT * FROM vault_ledgers ORDER BY 
                created_at DESC LIMIT ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    ledgers.add(mapVaultLedger(resultSet));
                }
            }

            return ledgers;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find top N: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public BigDecimal sumChangeAmountBycurrencyIdAndCreatedAtBetween(int currencyId, LocalDateTime start, LocalDateTime finish) {

        String sql = """
                SELECT SUM(change_amount) AS total_change
                FROM vault_ledgers WHERE currency_id = ?
                AND created_at between ? AND ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currencyId);
            statement.setString(2, start.toString());
            statement.setString(3, finish.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getBigDecimal("total_change");
                }
                return BigDecimal.ZERO;
            }



        } catch (SQLException e) {
            throw new RuntimeException("Error in calculate sum change amounts: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
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
