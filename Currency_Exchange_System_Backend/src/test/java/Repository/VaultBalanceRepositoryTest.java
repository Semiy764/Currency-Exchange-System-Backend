package Repository;

import org.example.App;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = App.class)
public class VaultBalanceRepositoryTest {

    @Autowired
    private VaultBalanceRepository vaultBalanceRepository;

    @Test
    public void savingVaultBalanceTest() {

        BigDecimal balance = new BigDecimal(200);
        VaultBalance vaultBalance = new VaultBalance(balance, 3, LocalDateTime.now());
        VaultBalance savedBalance = vaultBalanceRepository.save(vaultBalance);

        assertThat(savedBalance.getId()).isEqualTo(1);
        assertThat(savedBalance.getBalance()).isEqualTo(balance);
        assertThat(savedBalance.getCurrencyId()).isEqualTo(3);
        assertThat(savedBalance.getLastUpdated()).isNotNull();
    }
}
