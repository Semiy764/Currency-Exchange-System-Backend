package Repository;

import org.example.App;
import org.example.model.Currency;
import org.example.repository.interfaces.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void savingCurrencyTest() {
        Currency currency = new Currency("CNY", "Yuan", "¥");
        Currency savedCurrency = currencyRepository.save(currency);

        assertThat(savedCurrency.getId()).isEqualTo(3);
        assertThat(currency.getCode()).isEqualTo("CNY");
        assertThat(currency.getName()).isEqualTo("Yuan");
        assertThat(currency.getSymbol()).isEqualTo("¥");
    }

    @Test
    public void findAllCurrenciesTest() {

        List<Currency> allCurrencies = currencyRepository.findAll();
        for(Currency currency : allCurrencies) {
            System.out.println(currency.getId() + " - " +
                    currency.getCode() + " - " +
                    currency.getName() + " - " +
                    currency.getSymbol());
        }

    }
}
