package Repository;

import org.example.App;
import org.example.model.Currency;
import org.example.repository.interfaces.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void savingCurrencyTest() {
        Currency currency = new Currency("USD", "dollor", "$");
        Currency savedCurrency = currencyRepository.save(currency);

        assertThat(savedCurrency.getId()).isEqualTo(1);
        assertThat(currency.getCode()).isEqualTo("USD");
        assertThat(currency.getName()).isEqualTo("dollor");
        assertThat(currency.getSymbol()).isEqualTo("$");
    }
}
