package org.example.service.impl;

import org.example.model.VaultLedger;
import org.example.repository.interfaces.VaultLedgerRepository;
import org.example.service.interfaces.VaultLedgerService;
import org.springframework.stereotype.Service;

@Service
public class VaultLedgerServiceImpl implements VaultLedgerService {

    private final VaultLedgerRepository vaultLedgerRepository;

    public VaultLedgerServiceImpl(VaultLedgerRepository vaultLedgerRepository) {
        this.vaultLedgerRepository = vaultLedgerRepository;
    }

    @Override
    public VaultLedger recordEntry(VaultLedger vaultLedger) {

        if(vaultLedger == null) {
            throw new IllegalArgumentException("vault ledger cannot be null");
        }

        return vaultLedgerRepository.save(vaultLedger);
    }
}
