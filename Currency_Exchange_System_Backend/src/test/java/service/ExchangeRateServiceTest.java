package service;

import org.example.App;
import org.example.model.ExchangeRate;
import org.example.service.interfaces.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(classes = App.class)
public class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateService exchangeRateService;


    @Test
    public void saveRateTest() {

        ExchangeRate rate = exchangeRateService.setRate(2, new BigDecimal(250), new BigDecimal(260), 1);
        System.out.println(
                rate.getId() + " - " +
                rate.getCurrencyId() + " - " +
                rate.getBuyRate().toString() + " - " +
                rate.getsellRate() + " - " +
                rate.getEffectiveDate() + " - " +
                rate.getCreatedBy());
    }

    @Test
    public void findLateseRateTest() {

        ExchangeRate rate = exchangeRateService.getCurrentRate(1);

        System.out.println(
                rate.getId() + " - " +
                        rate.getCurrencyId() + " - " +
                        rate.getBuyRate().toString() + " - " +
                        rate.getsellRate() + " - " +
                        rate.getEffectiveDate() + " - " +
                        rate.getCreatedBy());
    }

    //150 - 250 260

    @Test
    public void findAllCurrenciesCurrencyRatesTest() {

        List<ExchangeRate> allRates = exchangeRateService.getAllCurrentRates();
        for(ExchangeRate rate : allRates) {
            System.out.println(
                    rate.getId() + " - " +
                            rate.getCurrencyId() + " - " +
                            rate.getBuyRate().toString() + " - " +
                            rate.getsellRate() + " - " +
                            rate.getEffectiveDate() + " - " +
                            rate.getCreatedBy());
        }
    }

    @Test
    public void getRateHistoryTest() {

        List<ExchangeRate> rates = exchangeRateService.getRateHistory(2);
        for(ExchangeRate rate : rates) {
            System.out.println(
                    rate.getId() + " - " +
                            rate.getCurrencyId() + " - " +
                            rate.getBuyRate().toString() + " - " +
                            rate.getsellRate() + " - " +
                            rate.getEffectiveDate() + " - " +
                            rate.getCreatedBy());
        }
    }

    @Test
    public void getRateHistoryBetweenTest() {

        LocalDateTime start = LocalDateTime.of(2026, 8, 16, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 8, 20, 23, 59, 59);
        List<ExchangeRate> rates = exchangeRateService.getRateHistoryBetween(2, start, end);

        for(ExchangeRate rate : rates) {
            System.out.println(
                    rate.getId() + " - " +
                            rate.getCurrencyId() + " - " +
                            rate.getBuyRate().toString() + " - " +
                            rate.getsellRate() + " - " +
                            rate.getEffectiveDate() + " - " +
                            rate.getCreatedBy());
        }
    }


}
