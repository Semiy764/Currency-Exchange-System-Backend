package service;

import org.example.App;
import org.example.model.ExchangeRate;
import org.example.service.interfaces.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest(classes = App.class)
public class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateService exchangeRateService;


    @Test
    public void saveRateTest() {

        ExchangeRate rate = exchangeRateService.setRate(1, new BigDecimal(100), new BigDecimal(110), 1);
        System.out.println(
                rate.getId() + " - " +
                rate.getCurrencyId() + " - " +
                rate.getBuyRate().toString() + " - " +
                rate.getsellRate() + " - " +
                rate.getEffectiveDate() + " - " +
                rate.getCreatedBy());
    }
}
