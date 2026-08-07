package Repository;

import org.example.App;
import org.example.model.ExchangeRate;
import org.example.repository.interfaces.ExchangeRatesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class ExchangeRatesRepositoryTest {

    @Autowired
    private ExchangeRatesRepository exchangeRatesRepository;

    @Test
    public void savingRatesTest() {

        BigDecimal buyRate = new BigDecimal(190000);
        BigDecimal sellRate = new BigDecimal(195000);

        ExchangeRate exchangeRate = new ExchangeRate(buyRate, sellRate, 7, LocalDateTime.now(), 3);
        ExchangeRate saved = exchangeRatesRepository.save(exchangeRate);

        assertThat(saved.getId()).isEqualTo(4);
        assertThat(saved.getBuyRate()).isEqualTo(buyRate);
        assertThat(saved.getsellRate()).isEqualTo(sellRate);
        assertThat(saved.getCurrencyId()).isEqualTo(3);
        assertThat(saved.getCreatedBy()).isEqualTo(7);
        assertThat(saved.getEffectiveDate()).isNotNull();
    }

    @Test
    public void testfindAllRates() {

        List<ExchangeRate> allRates = exchangeRatesRepository.findAll();

        for(ExchangeRate rate : allRates) {
            System.out.println(
                    rate.getId() + " - " +
                    rate.getBuyRate() + " - " +
                    rate.getsellRate() + " - " +
                    rate.getCreatedBy() + " - " +
                    rate.getEffectiveDate() + " - " +
                    rate.getCurrencyId()
                    );
        }
    }
}
