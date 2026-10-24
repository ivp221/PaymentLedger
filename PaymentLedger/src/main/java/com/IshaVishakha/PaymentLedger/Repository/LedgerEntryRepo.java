package com.IshaVishakha.PaymentLedger.Repository;

import com.IshaVishakha.PaymentLedger.Entity.LedgerEntry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface LedgerEntryRepo extends JpaRepository<LedgerEntry, UUID> {
    Page<LedgerEntry> findByAccountIdOrderByCreatedAtDesc(UUID accountId, Pageable pageable);

}
