package org.example.repository.impl;
import org.example.exception.ResourceNotFoundException;
import org.example.model.ExchangeRate;
import org.example.repository.interfaces.ExchangeRatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ExchangeRatesRepositoryImpl implements ExchangeRatesRepository {

    @Autowired
    private DataSource dataSource;

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

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ) {

            statement.setInt(1, exchangeRate.getCurrencyId().intValue());
            statement.setString(2, exchangeRate.getBuyRate().toString());
            statement.setString(3, exchangeRate.getsellRate().toString());
            statement.setString(4, exchangeRate.getEffectiveDate().toString());
            statement.setInt(5, exchangeRate.getCreatedBy().intValue());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if(keys.next()) {
                    exchangeRate.setId(keys.getLong(1));
                }
            }

            return exchangeRate;

        } catch (SQLException e) {
            throw new RuntimeException("Error in save exchange rate: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<ExchangeRate> findAll() {

        List<ExchangeRate> allRates = new ArrayList<>();
        String sql = """
                SELECT * FROM exchange_rates
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                ) {

            while (resultSet.next()) {
                allRates.add(mapRates(resultSet));
            }
            return allRates;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all exchange rate: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
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

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            String justDate = LocalDate.now().toString();
            statement.setString(1, justDate);
            statement.setInt(2, currency_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapRates(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in find last rate: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
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

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, currencyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    rates.add(mapRates(resultSet));
                }
            }

            return rates;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all rates of currencies: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
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

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {


            String justDate = LocalDate.now().toString();
            statement.setString(1, justDate);
            statement.setInt(2, currencyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    rates.add(mapRates(resultSet));
                }
            }
            return rates;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find all currency rates today: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public void delete(int exchangeRateId) {
        String sql = """
                DELETE FROM exchange_rates WHERE id = ?
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, exchangeRateId);
            int rows = statement.executeUpdate();

            if(rows == 0) {
                throw new ResourceNotFoundException("Exchange rate not found with ID: " + exchangeRateId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error in delete currency rates: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<ExchangeRate> findByCreatedBy(int userId) {
        List<ExchangeRate> rates = new ArrayList<>();
        String sql = """
                SELECT * FROM exchange_rates
                WHERE created_by = ?
                ORDER BY effective_date
                DESC
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rates.add(mapRates(resultSet));
                }
            }

            return rates;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find rate by created by " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    @Override
    public List<ExchangeRate> findByCurrencyIdAndEffectiveDateBetween(int currencyId, LocalDateTime start, LocalDateTime end) {

        List<ExchangeRate> rates = new ArrayList<>();
        String sql = """
                SELECT * FROM exchange_rates WHERE
                effective_date BETWEEN ? AND ?
                and currency_id = ?
                ORDER BY effective_date DESC
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, start.toString());
            statement.setString(2, end.toString());
            statement.setInt(3, currencyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    rates.add(mapRates(resultSet));
                }
            }
            return rates;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find by currencyId and effective date between: " + e, e);

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<ExchangeRate> findLatestRateForAllCurrencies() {

        List<ExchangeRate> rates = new ArrayList<>();
        String sql = """
                SELECT * FROM exchange_rates WHERE
                effective_date IN (
                SELECT MAX (effective_date)
                FROM exchange_rates
                GROUP BY currency_id)
                ORDER BY currency_id
                """;

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try(
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {


            while(resultSet.next()) {
                rates.add(mapRates(resultSet));
            }
            return rates;

        } catch (SQLException e) {
            throw new RuntimeException("Error in find latest rate for all currencies: " + e, e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

    }

    private ExchangeRate mapRates(ResultSet resultSet) throws SQLException {

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(resultSet.getInt("id"));
        exchangeRate.setCurrencyId(resultSet.getInt("currency_id"));
        exchangeRate.setBuyRate(new BigDecimal(resultSet.getString("buy_rate")));
        exchangeRate.setSellRate(new BigDecimal(resultSet.getString("sell_rate")));
        exchangeRate.setEffectiveDate(LocalDateTime.parse(resultSet.getString("effective_date")));
        exchangeRate.setCreatedBy(resultSet.getInt("created_by"));

        return exchangeRate;
    }
}
