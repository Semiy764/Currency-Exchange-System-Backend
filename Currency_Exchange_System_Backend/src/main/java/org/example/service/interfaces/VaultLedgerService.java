package org.example.service.interfaces;

import org.example.dto.response.VaultSummaryDto;
import org.example.model.VaultLedger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VaultLedgerService {

    VaultLedger recordEntry(VaultLedger vaultLedger);
    List<VaultLedger> getHistory(int currencyId);
    List<VaultLedger> getHistoryBetween(int currencyId, LocalDateTime start, LocalDateTime end);
    List<VaultLedger> getByPerformedByUser(int userId);
    BigDecimal sumChangesBetween(int currencyId, LocalDateTime start, LocalDateTime end);
    boolean reconcile(int currencyId, BigDecimal currenctBalance);
    VaultSummaryDto getVaultSummary(BigDecimal threshold);
}
