package org.example.repository.impl;

import jdk.jfr.Registered;
import org.example.database.DatabaseManager;
import org.example.model.Transaction;
import org.example.repository.interfaces.TransactionRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.HTMLDocument;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
