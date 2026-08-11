package service;

import org.example.App;
import org.example.model.Currency;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.example.service.interfaces.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

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

        Currency currency = new Currency("USD", "Dolor", "$");
        Currency saved = currencyService.addCurrency(currency);
        VaultBalance vaultBalance = vaultBalanceRepository.findByCurrencyId(1);

        assertThat(saved.getCode()).isEqualTo("USD");
        assertThat(saved.getName()).isEqualTo("Dolor");
        assertThat(saved.getSymbol()).isEqualTo("$");
        assertThat(vaultBalance.getBalance()).isEqualTo(new BigDecimal(0));
        assertThat(vaultBalance.getLastUpdated()).isNotNull();
        assertThat(vaultBalance.getId()).isEqualTo(1);
        assertThat(vaultBalance.getCurrencyId()).isEqualTo(1);
    }
}
