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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
public class VaultLedgerRepositoryTest {

    @Autowired
    private VaultLedgerRepository vaultLedgerRepository;

    @Test
    public void savingVaultLedgerTest() {

        BigDecimal changeAmount = new BigDecimal(1024);
        VaultLedger vaultLedger = new VaultLedger(changeAmount, LocalDateTime.now(), 2, 6, LedgerReason.DEPOSIT);
        VaultLedger saved = vaultLedgerRepository.save(vaultLedger);

//        assertThat(saved).isNotNull();
//        assertThat(saved.getChangeAmount()).isEqualTo(changeAmount);
//        assertThat(saved.getPreformedByUserId()).isEqualTo(7);
//        assertThat(saved.getCurrencyId()).isEqualTo(3);
//        assertThat(saved.getReason().name()).isEqualTo("WITHDRAW");

    }

    @Test
    public void findVaultLedgerByIdTest() {

        VaultLedger ledger = vaultLedgerRepository.findById(1);

        BigDecimal changeAmount = new BigDecimal(782);
        assertThat(ledger).isNotNull();
        assertThat(ledger.getChangeAmount()).isEqualTo(changeAmount);
        assertThat(ledger.getPreformedByUserId()).isEqualTo(7);
        assertThat(ledger.getCurrencyId()).isEqualTo(3);
        assertThat(ledger.getReason().name()).isEqualTo("WITHDRAW");
    }

    @Test
    public void findAllLedgersTest() {
        List<VaultLedger> allLedgers = vaultLedgerRepository.findAll();
        for(VaultLedger ledger : allLedgers) {
            System.out.println(ledger.getId() + " - " +
                    ledger.getCurrencyId() + " - " +
                    ledger.getPreformedByUserId() + " - " +
                    ledger.getReason().name() + " - " +
                    ledger.getCreatedAt() + " - " +
                    ledger.getChangeAmount()
                    );
        }
    }

    @Test
    public void findByCurrencyIdOrderByCreatedAtDescTest() {

        List<VaultLedger> foundLedgers = vaultLedgerRepository.findByCurrencyIdOrderByCreatedAtDesc(2);
        for(VaultLedger ledger : foundLedgers) {
            System.out.println(ledger.getId() + " - " +
                    ledger.getCurrencyId() + " - " +
                    ledger.getPreformedByUserId() + " - " +
                    ledger.getReason().name() + " - " +
                    ledger.getCreatedAt() + " - " +
                    ledger.getChangeAmount()
            );
        }
    }

    @Test
    public void findByCurrencyIdAndCreatedAtBetweenTest() {

        LocalDateTime start = LocalDateTime.of(2025, 8, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2028, 8, 7, 23, 59, 59);
        List<VaultLedger> ledgers = vaultLedgerRepository.findByCurrencyIdAndCreatedAtBetween(2, start, end);
        for(VaultLedger vaultLedger : ledgers) {
            System.out.println(vaultLedger.getId() + " - " +
                    vaultLedger.getCurrencyId() + " - " +
                    vaultLedger.getPreformedByUserId() + " - " +
                    vaultLedger.getReason().name() + " - " +
                    vaultLedger.getCreatedAt() + " - " +
                    vaultLedger.getChangeAmount()
            );
        }
    }

    @Test
    public void findByPerformedByUserIdTest() {

        List<VaultLedger> foundLedgers = vaultLedgerRepository.findByPerformedByUserId(6);
        for(VaultLedger ledger : foundLedgers) {
            System.out.println(ledger.getId() + " - " +
                    ledger.getCurrencyId() + " - " +
                    ledger.getPreformedByUserId() + " - " +
                    ledger.getReason().name() + " - " +
                    ledger.getCreatedAt() + " - " +
                    ledger.getChangeAmount()
            );
        }
    }

    @Test
    public void findTopNByOrderByCreatedAtDescTest() {

        List<VaultLedger> ledgers = vaultLedgerRepository.findTopNByOrderByCreatedAtDesc(4);
        for(VaultLedger ledger : ledgers) {
            System.out.println(ledger.getId() + " - " +
                    ledger.getCurrencyId() + " - " +
                    ledger.getPreformedByUserId() + " - " +
                    ledger.getReason().name() + " - " +
                    ledger.getCreatedAt() + " - " +
                    ledger.getChangeAmount()
            );
        }
    }
}
