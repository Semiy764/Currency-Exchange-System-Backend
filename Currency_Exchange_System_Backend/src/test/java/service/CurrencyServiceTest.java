package service;

import org.example.App;
import org.example.model.Currency;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.example.service.interfaces.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = App.class)
public class CurrencyServiceTest {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private VaultBalanceRepository vaultBalanceRepository;

    @Test
    public void savingCurrencyTest() {

        Currency currency = new Currency("EUR", "Euro", "&&");
        currencyService.addCurrency(currency);
//        VaultBalance vaultBalance = vaultBalanceRepository.findByCurrencyId(1);

//        assertThat(saved.getCode()).isEqualTo("USD");
//        assertThat(saved.getName()).isEqualTo("Dolor");
//        assertThat(saved.getSymbol()).isEqualTo("$");
//        assertThat(vaultBalance.getBalance()).isEqualTo(new BigDecimal(0));
//        assertThat(vaultBalance.getLastUpdated()).isNotNull();
//        assertThat(vaultBalance.getId()).isEqualTo(1);
//        assertThat(vaultBalance.getCurrencyId()).isEqualTo(1);
    }

    @Test
    public void findingAllCurrenciesTest() {

        List<Currency> allCurrencies = currencyService.findAll();
        List<VaultBalance> vaultBalances = vaultBalanceRepository.findAll();
        for(Currency currency : allCurrencies) {
            System.out.println(
                    currency.getId() + " - " +
                    currency.getCode() + " - " +
                    currency.getName() + " - " +
                    currency.getSymbol());
        }

        for(VaultBalance vaultBalance : vaultBalances) {
            System.out.println(
                    vaultBalance.getId() + " - " +
                    vaultBalance.getBalance() + " - " +
                    vaultBalance.getCurrencyId() + " - " +
                    vaultBalance.getLastUpdated()
                    );
        }
    }

    @Test
    public void findingAllActiveCurrenciesTest() {

        List<Currency> activeCurrencies = currencyService.findAllActiveCurrencies();
        for(Currency currency : activeCurrencies) {
            System.out.println(
                    currency.getId() + " - " +
                            currency.getCode() + " - " +
                            currency.getName() + " - " +
                            currency.getSymbol()
            );
        }
    }

    @Test
    public void existingCurrencyByCodeTest() {
        System.out.println(currencyService.existsByCode("EUD"));
    }

}
