package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.model.ExchangeRate;
import org.example.repository.interfaces.ExchangeRatesRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class ExchangeRatesRepositoryImpl implements ExchangeRatesRepository {

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        String sql = """
                INSERT INTO exchange_rates (
                currency_id,
                buy_rate,
                sell_rate,
                effective_date,
                created_by)
                VALUES(?, ?, ?, ?, ?)
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ) {

            statement.setInt(1, exchangeRate.getCurrencyId().intValue());
            statement.setString(2, exchangeRate.getBuyRate().toString());
            statement.setString(3, exchangeRate.getsellRate().toString());
            statement.setString(4, exchangeRate.getEffectiveDate().toString());
            statement.setInt(5, exchangeRate.getCreatedBy().intValue());

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()) {
                exchangeRate.setId(keys.getLong(1));
            }

            return exchangeRate;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save exchange rate: " + e, e);
        }
    }
}
