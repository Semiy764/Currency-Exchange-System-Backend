package service;

import org.example.App;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.example.service.interfaces.VaultBalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(classes = App.class)
public class VaultBalanceServiceTest {

    @Autowired
    private VaultBalanceService vaultBalanceService;

    @Autowired
    private VaultBalanceRepository vaultBalanceRepository;

    @Test
    public void findVaultBalanceByCurrencyIdTest() {

        VaultBalance vaultBalance = vaultBalanceService.getBalance(1);
        System.out.println(
                vaultBalance.getId() + " - " +
                vaultBalance.getBalance().intValue() + " - " +
                vaultBalance.getCurrencyId().toString() + " - " +
                vaultBalance.getLastUpdated()
        );

    }

    @Test
    public void findAllVaultBalancesTest() {

        List<VaultBalance> allBalances = vaultBalanceService.getAllBalances();
        for(VaultBalance vaultBalance : allBalances) {
            System.out.println(
                    vaultBalance.getId() + " - " +
                            vaultBalance.getBalance().intValue() + " - " +
                            vaultBalance.getCurrencyId().toString() + " - " +
                            vaultBalance.getLastUpdated()
            );
        }
    }

    @Test
    public void getLowBalancesTest() {

        List<VaultBalance> balances = vaultBalanceService.getLowBalances(new BigDecimal(100));
        for(VaultBalance vaultBalance : balances) {
            System.out.println(
                    vaultBalance.getId() + " - " +
                            vaultBalance.getBalance().intValue() + " - " +
                            vaultBalance.getCurrencyId().toString() + " - " +
                            vaultBalance.getLastUpdated()
            );
        }
    }

    @Test
    public void depositTest() {

        vaultBalanceService.deposit(1, new BigDecimal(1000), 1);
        VaultBalance vaultBalance = vaultBalanceRepository.findByCurrencyId(1);
        System.out.println(vaultBalance.getBalance());
    }

    @Test
    public void withdrawTest() {

        vaultBalanceService.withdraw(1, new BigDecimal(-100), 1);
        VaultBalance vaultBalance = vaultBalanceRepository.findById(1);
        System.out.println(vaultBalance.getBalance());
    }

    @Test
    public void hasSufficientBalanceTest() {
        System.out.println(vaultBalanceService.hasSufficientBalance(1, new BigDecimal(301)));
    }
}
