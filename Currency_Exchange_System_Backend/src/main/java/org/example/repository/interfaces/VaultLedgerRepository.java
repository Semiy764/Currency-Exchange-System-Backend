package org.example.repository.interfaces;

import org.example.model.VaultBalance;
import org.example.model.VaultLedger;

import java.util.List;

public interface VaultLedgerRepository {

    VaultLedger save(VaultLedger vaultLedger);
    List<VaultLedger> findAll();
}
