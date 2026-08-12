package Repository;

import org.example.App;
import org.example.model.Currency;
import org.example.model.ExchangeRate;
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
        System.out.println(savedCurrency.getId());

//        assertThat(savedCurrency.getId()).isEqualTo(3);
//        assertThat(currency.getCode()).isEqualTo("CNY");
//        assertThat(currency.getName()).isEqualTo("Yuan");
//        assertThat(currency.getSymbol()).isEqualTo("¥");
    }

    @Test
    public void findAllCurrenciesTest() {

        List<Currency> allCurrencies = currencyRepository.findAll();
        for(Currency currency : allCurrencies) {
            System.out.println(currency.getId() + " - " +
                    currency.getCode() + " - " +
                    currency.getName() + " - " +
                    currency.getSymbol() + " - " +
                    currency.isActive());
        }

    }

    @Test
    public void testFindCurrencyById() {
        Currency currency = currencyRepository.findById(3);
        assertThat(currency.getId()).isEqualTo(3);
        assertThat(currency.getCode()).isEqualTo("CNY");
        assertThat(currency.getName()).isEqualTo("Yuan");
        assertThat(currency.getSymbol()).isEqualTo("¥");
    }

    @Test
    public void findCurrencyBySymbolTest() {

        Currency currency = currencyRepository.findBySymbol("¥");
        assertThat(currency.getId()).isEqualTo(3);
        assertThat(currency.getCode()).isEqualTo("CNY");
        assertThat(currency.getName()).isEqualTo("Yuan");
        assertThat(currency.getSymbol()).isEqualTo("¥");
    }

    @Test
    public void findingCurrencyByNameTest() {

        Currency currency = currencyRepository.findByName("Yuan");
        assertThat(currency.getId()).isEqualTo(3);
        assertThat(currency.getCode()).isEqualTo("CNY");
        assertThat(currency.getName()).isEqualTo("Yuan");
        assertThat(currency.getSymbol()).isEqualTo("¥");
    }

    @Test
    public void findCurrencyByCodeTest() {

        Currency currency = currencyRepository.findByCode("CNY");
        assertThat(currency.getId()).isEqualTo(3);
        assertThat(currency.getCode()).isEqualTo("CNY");
        assertThat(currency.getName()).isEqualTo("Yuan");
        assertThat(currency.getSymbol()).isEqualTo("¥");
    }
    // exist ha

    @Test
    public void deleteCurrencyTest() {
        currencyRepository.delete(1);
    }

    @Test
    public void existingCurrencyByIdTest() {
        System.out.println(currencyRepository.existsById(1));
    }

    @Test
    public void existingCurrencyByNameTest() {
        System.out.println(currencyRepository.existsByName("Yua"));
    }

    @Test
    public void existingCurrencyByCodeTest() {
        System.out.println(currencyRepository.existsByCode("CNN"));
    }

    @Test
    public void updatingCurrencyTest() {
        Currency currency = currencyRepository.findById(3);
        currency.setName("dollor");
        currency.setCode("USD");
        currency.setSymbol("$");

        currencyRepository.update(currency);

        Currency foundCurrency = currencyRepository.findById(3);
        assertThat(foundCurrency.getId()).isEqualTo(3);
        assertThat(foundCurrency.getName()).isEqualTo("dollor");
        assertThat(foundCurrency.getCode()).isEqualTo("USD");
        assertThat(foundCurrency.getSymbol()).isEqualTo("$");

    }

    @Test
    public void findActiveCurreciesTest() {

        List<Currency> foundCurrencies = currencyRepository.findAllActiveCurrencies();
        for(Currency currency : foundCurrencies) {
            System.out.println(
                    currency.getId() + " - " +
                    currency.getCode() + " - " +
                    currency.getName() + " - " +
                    currency.getSymbol()
            );
        }
    }

    @Test
    public void deactivateCurrencyTest() {

        currencyRepository.deactivateCurrency(1);
//        Currency currency = currencyRepository.findById(2);
//        assertThat(currency.isActive()).isEqualTo(false);
    }

    @Test
    public void activateCurrencyTest() {
        currencyRepository.activateCurrency(2);
        Currency currency = currencyRepository.findById(2);
//        assertThat(currency.isActive()).isEqualTo(true);
    }

    @Test
    public void isActiveTest() {
        System.out.println(currencyRepository.isActive(2));
    }

}
