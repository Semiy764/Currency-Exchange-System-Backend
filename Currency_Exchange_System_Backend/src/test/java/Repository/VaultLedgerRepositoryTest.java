package Repository;

import org.example.App;
import org.example.enums.LedgerReason;
import org.example.model.VaultLedger;
import org.example.repository.interfaces.VaultLedgerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
public class VaultLedgerRepositoryTest {

    @Autowired
    private VaultLedgerRepository vaultLedgerRepository;

    @Test
    public void savingVaultLedgerTest() {

        BigDecimal changeAmount = new BigDecimal(782);
        VaultLedger vaultLedger = new VaultLedger(changeAmount, LocalDateTime.now(), 3, 7, LedgerReason.WITHDRAW);
        VaultLedger saved = vaultLedgerRepository.save(vaultLedger);

        assertThat(saved).isNotNull();
        assertThat(saved.getChangeAmount()).isEqualTo(changeAmount);
        assertThat(saved.getPreformedByUserId()).isEqualTo(7);
        assertThat(saved.getCurrencyId()).isEqualTo(3);
        assertThat(saved.getReason().name()).isEqualTo("WITHDRAW");

    }
}
