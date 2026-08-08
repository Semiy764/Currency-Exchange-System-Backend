package org.example.repository.interfaces;

import org.example.model.VaultBalance;
import org.example.model.VaultLedger;

public interface VaultLedgerRepository {

    VaultLedger save(VaultLedger vaultLedger);
}
