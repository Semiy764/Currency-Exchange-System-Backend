package service;

import org.example.App;
import org.example.enums.LedgerReason;
import org.example.model.VaultBalance;
import org.example.model.VaultLedger;
import org.example.repository.interfaces.VaultLedgerRepository;
import org.example.service.interfaces.VaultLedgerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = App.class)
public class VaultLedgerServiceTest {

    @Autowired
    private VaultLedgerService vaultLedgerService;

    @Autowired
    private VaultLedgerRepository vaultLedgerRepository;

    @Test
    public void savingVaultLedgerTest() {

        VaultLedger vaultLedger = new VaultLedger(new BigDecimal(100), LocalDateTime.now(), 1, 1, LedgerReason.WITHDRAW);
        vaultLedgerService.recordEntry(vaultLedger);

        VaultLedger found = vaultLedgerRepository.findById(1);

        assertThat(found.getId()).isEqualTo(1);
        assertThat(found.getReason()).isEqualTo(LedgerReason.WITHDRAW);
        assertThat(found.getCreatedAt()).isNotNull();
        assertThat(found.getCurrencyId()).isEqualTo(1);
        assertThat(found.getChangeAmount()).isEqualTo(new BigDecimal(100));
    }

    @Test
    public void getHistoryTest() {

        List<VaultLedger> ledgers = vaultLedgerService.getHistory(2);
        for(VaultLedger vaultLedger : ledgers) {
            System.out.println(
                    vaultLedger.getId() + " - " +
                    vaultLedger.getPreformedByUserId() + " - " +
                    vaultLedger.getChangeAmount().toString() + " - " +
                    vaultLedger.getCurrencyId().toString() + " - " +
                    vaultLedger.getReason() + " - " +
                    vaultLedger.getChangeAmount().toString());
        }
    }
}
