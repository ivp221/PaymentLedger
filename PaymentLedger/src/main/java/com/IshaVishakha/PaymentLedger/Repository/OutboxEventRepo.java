package com.IshaVishakha.PaymentLedger.Repository;

import com.IshaVishakha.PaymentLedger.Entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepo extends JpaRepository<OutboxEvent, UUID> {

    @Query(value = "SELECT o.* FROM outbox_events o WHERE published_at IS NULL ORDER BY created_at ASC limit :limit FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<OutboxEvent> findUnpublishedBatchForUpdate(@Param("limit") int limit);
}
