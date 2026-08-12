package Repository;

import org.assertj.core.api.AssertionsForClassTypes;
import org.example.App;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = App.class)
public class VaultBalanceRepositoryTest {

    @Autowired
    private VaultBalanceRepository vaultBalanceRepository;

    @Test
    public void savingVaultBalanceTest() {

        BigDecimal balance = new BigDecimal(2100);
        VaultBalance vaultBalance = new VaultBalance(balance, 3, LocalDateTime.now());
        VaultBalance savedBalance = vaultBalanceRepository.save(vaultBalance);

//        assertThat(savedBalance.getId()).isEqualTo(2);
        assertThat(savedBalance.getBalance()).isEqualTo(balance);
        assertThat(savedBalance.getCurrencyId()).isEqualTo(3);
        assertThat(savedBalance.getLastUpdated()).isNotNull();
    }

    @Test
    public void findAllBalancesTest() {
        List<VaultBalance> allBalances = vaultBalanceRepository.findAll();
        for(VaultBalance balance : allBalances) {
            System.out.println(balance.getId() + " - " +
                    balance.getBalance().toString() + " - " +
                    balance.getCurrencyId() + " - " +
                    balance.getLastUpdated());
        }
    }

    @Test
    public void existsByCurrencyIdTest() {
        System.out.println(vaultBalanceRepository.existsByCurrencyId(4));
    }

    @Test
    public void findByCurrencyIdTest() {
        VaultBalance balance = vaultBalanceRepository.findByCurrencyId(3);

        BigDecimal balanceAmount = new BigDecimal(200);
        AssertionsForClassTypes.assertThat(balance.getId()).isEqualTo(1);
        AssertionsForClassTypes.assertThat(balance.getBalance()).isEqualTo(balanceAmount);
        AssertionsForClassTypes.assertThat(balance.getCurrencyId()).isEqualTo(3);
        AssertionsForClassTypes.assertThat(balance.getLastUpdated()).isNotNull();

    }

    @Test
    public void adjustBalanceTest() {
        vaultBalanceRepository.adjustBalance(1, new BigDecimal(100));
        VaultBalance balance = vaultBalanceRepository.findByCurrencyId(1);
        System.out.println(balance.getBalance());
//        assertThat(balance.getBalance().intValue()).isEqualTo(435);
    }

    @Test
    public void deleteBalanceTest() {
        vaultBalanceRepository.deleteById(1);
    }

    @Test
    public void findBalanceByIdTest() {
        VaultBalance balance = vaultBalanceRepository.findById(2);
        BigDecimal balanceAmount = new BigDecimal(435);

        AssertionsForClassTypes.assertThat(balance.getId()).isEqualTo(2);
        AssertionsForClassTypes.assertThat(balance.getBalance()).isEqualTo(balanceAmount);
        AssertionsForClassTypes.assertThat(balance.getCurrencyId()).isEqualTo(2);
        AssertionsForClassTypes.assertThat(balance.getLastUpdated()).isNotNull();

    }

    @Test
    public void findBalancLessThanTest() {
        List<VaultBalance> balances = vaultBalanceRepository.findByBalanceLessThan(new BigDecimal(5000));
        for(VaultBalance balance : balances) {
            System.out.println(balance.getId() + " - " +
                    balance.getBalance().toString() + " - " +
                    balance.getCurrencyId() + " - " +
                    balance.getLastUpdated());
        }
    }
}
