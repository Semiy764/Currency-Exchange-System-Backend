package org.example.repository.impl;

import jdk.jfr.Registered;
import org.example.database.DatabaseManager;
import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.model.Transaction;
import org.example.repository.interfaces.TransactionRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.HTMLDocument;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Override
    public Transaction save(Transaction transaction) {

        String sql = """
                INSERT INTO transactions (
                type,
                currency_id,
                customer_id,
                amount_currency,
                amount_toman,
                requested_rate,
                rate_used,
                requested_by_customer,
                performed_by_userId,
                approved_by_userId,
                created_at,
                approved_at,
                status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ) {

            statement.setString(1, transaction.getTxType().name());
            statement.setInt(2, transaction.getCurrencyId().intValue());
            statement.setInt(3, transaction.getCustomerId().intValue());
            statement.setString(4, transaction.getAmountCurrency().toString());
            statement.setString(5, transaction.getAmountToman().toString());
            statement.setString(6, transaction.getRequestedRate().toString());
            statement.setString(7, transaction.getRateUsed().toString());
            statement.setInt(8, transaction.isRequestedByCustomer() ? 1 : 0);
            statement.setObject(9, transaction.getPerformedByUserId());
            statement.setObject(10, transaction.getApprovedByUserId());
            statement.setString(11, transaction.getCreatedAt().toString());
            statement.setObject(12, transaction.getApprovedAt());
            statement.setString(13, transaction.getStatus().name());

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                transaction.setId(keys.getLong(1));
            }

            return transaction;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save transaction:" + e, e);
        }



    }

    @Override
    public List<Transaction> findAll() {

        List<Transaction> allTransactions = new ArrayList<>();
        String sql = """
                SELECT * FROM transactions
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                allTransactions.add(mapTranasction(resultSet));
            }

            return allTransactions;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all transactions:" + e, e);

        }
    }

    @Override
    public Transaction findById(int id) {

        String sql = """
                SELECT * FROM transactions WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return mapTranasction(resultSet);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find transaction by id:" + e, e);
        }
    }

    @Override
    public List<Transaction> findAllByOrderByCreatedAtDesc() {

        List<Transaction> allTransactions = new ArrayList<>();
        String sql = """
                SELECT * FROM transactions
                ORDER BY created_at DESC
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                allTransactions.add(mapTranasction(resultSet));
            }

            return allTransactions;


        } catch (SQLException e) {
            throw new RuntimeException("Error in find transactions order by created at desc:" + e, e);
        }
    }

    @Override
    public List<Transaction> findByCustomerIdOrderByCreatedAtDesc(int customerId) {

        List<Transaction> allTrans = new ArrayList<>();
        String sql = """
                SELECT * FROM transactions WHERE customer_id = ?
                ORDER BY created_at DESC
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                allTrans.add(mapTranasction(resultSet));
            }
            return allTrans;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find transactions by customer id order by desc: " + e, e);
        }
    }

    @Override
    public List<Transaction> findByStatusOrderByCreatedAtDesc(TxStatus status) {

        List<Transaction> transactions = new ArrayList<>();
        String sql = """
                SELECT * FROM transactions WHERE status = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setString(1, status.name());
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                transactions.add(mapTranasction(resultSet));
            }
            return transactions;


        } catch (SQLException e) {
            throw new RuntimeException("Error in find transactions by status order by desc: " + e, e);
        }
    }

    @Override
    public List<Transaction> findByPreformedByUserId(int userId) {

        List<Transaction> allTrans = new ArrayList<>();
        String sql = """
                SELECT * FROM transactions 
                WHERE performed_by_userId = ? 
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                allTrans.add(mapTranasction(resultSet));
            }
            return allTrans;


        } catch (SQLException e) {
            throw new RuntimeException("Error in find transactions by performed by user id: " + e, e);
        }
    }

    @Override
    public List<Transaction> findByApprovedByUserId(int userId) {

        List<Transaction> trans = new ArrayList<>();
        String sql = """
                SELECT * FROM transactions 
                WHERE approved_by_userId = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                trans.add(mapTranasction(resultSet));
            }

            return trans;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find transactions by approved by user id: " + e, e);
        }
    }

    @Override
    public List<Transaction> findByCurrencyIdAndCreatedAtBetween(int currencyId, LocalDateTime start, LocalDateTime finish) {

        List<Transaction> trans = new ArrayList<>();
        String sql = """
                SELECT * FROM transactions WHERE currency_id = ?
                AND created_at BETWEEN ? AND ?
                ORDER BY created_at DESC 
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, currencyId);
            statement.setString(2, start.toString());
            statement.setString(3, finish.toString());

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                trans.add(mapTranasction(resultSet));
            }

            return trans;


        } catch (SQLException e) {
            throw new RuntimeException("Error in find transactions by currency id and dates between: " + e, e);
        }
    }

    private Transaction mapTranasction(ResultSet resultSet) throws SQLException {

        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getLong("id"));
        transaction.setTxType(TxType.valueOf(resultSet.getString("type")));
        transaction.setCurrencyId(resultSet.getLong("currency_id"));
        transaction.setCustomerId(resultSet.getLong("customer_id"));
        transaction.setAmountCurrency(new BigDecimal(resultSet.getString("amount_currency")));
        transaction.setAmountToman(new BigDecimal(resultSet.getString("amount_toman")));
        transaction.setRequestedRate(new BigDecimal(resultSet.getString("requested_rate")));
        transaction.setRateUsed(new BigDecimal(resultSet.getString("rate_used")));
        transaction.setRequestedByCustomer(resultSet.getInt("requested_by_customer") == 1);

        long performedByUserIdRaw = resultSet.getLong("performed_by_userId");
        transaction.setPerformedByUserId(resultSet.wasNull() ? null : performedByUserIdRaw);

        long approvedByUserIdRaw = resultSet.getLong("approved_by_userId");
        transaction.setApprovedByUserId(resultSet.wasNull() ? null : approvedByUserIdRaw);

        transaction.setCreatedAt(LocalDateTime.parse(resultSet.getString("created_at")));

        String approvedAt = resultSet.getObject("approved_at", String.class);
        transaction.setApprovedAt(approvedAt != null ? LocalDateTime.parse(approvedAt) : null);

        transaction.setStatus(TxStatus.valueOf(resultSet.getString("status")));

        return transaction;

    }
}
