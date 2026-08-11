package service;

import org.example.App;
import org.example.model.VaultBalance;
import org.example.service.interfaces.VaultBalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = App.class)
public class VaultBalanceServiceTest {

    @Autowired
    private VaultBalanceService vaultBalanceService;

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
}
