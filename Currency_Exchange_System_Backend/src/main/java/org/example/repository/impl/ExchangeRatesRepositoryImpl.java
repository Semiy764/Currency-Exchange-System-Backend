package org.example.repository.impl;

import org.example.database.DatabaseManager;
import org.example.model.ExchangeRate;
import org.example.repository.interfaces.ExchangeRatesRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ExchangeRate> findAll() {

        List<ExchangeRate> allRates = new ArrayList<>();
        String sql = """
                SELECT * FROM exchange_rates
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                allRates.add(mapRates(resultSet));
            }

            return allRates;


        } catch (SQLException e) {
            throw new RuntimeException("Error in find all exchange rate: " + e, e);

        }
    }

    @Override
    public ExchangeRate findLastRateToday(int currency_id) {

        String sql = """
                SELECT * FROM exchange_rates
                WHERE DATE (effective_date) = ?
                AND currency_id = ?
                ORDER BY effective_date DESC
                LIMIT 1
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            String justDate = LocalDate.now().toString();
            statement.setString(1, justDate);
            statement.setInt(2, currency_id);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return mapRates(resultSet);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find last rate: " + e, e);
        }
    }

    @Override
    public List<ExchangeRate> findAllRatesOfCurrency(int currencyId) {

        List<ExchangeRate> rates = new ArrayList<>();
        String sql = """
                SELECT * FROM exchange_rates
                WHERE currency_id = ?
                ORDER BY effective_date DESC
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, currencyId);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                rates.add(mapRates(resultSet));
            }
            return rates;


        } catch (SQLException e) {
            throw new RuntimeException("Error in find all rates of currency: " + e, e);

        }
    }

    @Override
    public List<ExchangeRate> findAllCurrencyRatesToday(int currencyId) {

        List<ExchangeRate> rates = new ArrayList<>();
        String sql = """
                SELECT * FROM exchange_rates 
                WHERE DATE(effective_date) = ?
                AND currency_id = ?
                ORDER BY effective_date DESC
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {


            String justDate = LocalDate.now().toString();
            statement.setString(1, justDate);
            statement.setInt(2, currencyId);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                rates.add(mapRates(resultSet));
            }
            return rates;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all currency rates today: " + e, e);

        }
    }

    @Override
    public void delete(int exchangeRateId) {
        String sql = """
                DELETE FROM exchange_rates WHERE id = ?
                """;

        try(
                Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {

            statement.setInt(1, exchangeRateId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error in delete currency rates: " + e, e);
        }
    }

    private ExchangeRate mapRates(ResultSet resultSet) throws SQLException {

        ExchangeRate exchangeRate = new ExchangeRate();
        // id currncyid buyrate sellrate
        exchangeRate.setId(resultSet.getInt("id"));
        exchangeRate.setCurrencyId(resultSet.getInt("currency_id"));
        exchangeRate.setBuyRate(new BigDecimal(resultSet.getString("buy_rate")));
        exchangeRate.setSellRate(new BigDecimal(resultSet.getString("sell_rate")));
        exchangeRate.setEffectiveDate(LocalDateTime.parse(resultSet.getString("effective_date")));
        exchangeRate.setCreatedBy(resultSet.getInt("created_by"));

        return exchangeRate;
    }
}
