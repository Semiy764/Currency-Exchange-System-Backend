package org.example.service.interfaces;

import org.example.model.VaultLedger;

import java.util.List;

public interface VaultLedgerService {

    VaultLedger recordEntry(VaultLedger vaultLedger);
    List<VaultLedger> getHistory(int currencyId);
}
