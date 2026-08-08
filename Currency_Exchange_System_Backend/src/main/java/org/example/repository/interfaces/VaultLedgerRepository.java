package org.example.repository.interfaces;

import org.example.model.VaultBalance;
import org.example.model.VaultLedger;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public interface VaultLedgerRepository {

    VaultLedger save(VaultLedger vaultLedger);
    List<VaultLedger> findAll();
    VaultLedger findById(int id);
    List<VaultLedger> findByCurrencyIdOrderByCreatedAtDesc(int currencyId);
    List<VaultLedger> findByCurrencyIdAndCreatedAtBetween(int currencyId,
                                                          LocalDateTime start,
                                                          LocalDateTime end);

    List<VaultLedger> findByPerformedByUserId(int userId);

}
