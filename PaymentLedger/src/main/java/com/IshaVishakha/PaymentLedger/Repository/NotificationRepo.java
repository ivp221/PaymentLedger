package com.IshaVishakha.PaymentLedger.Repository;

import com.IshaVishakha.PaymentLedger.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepo extends JpaRepository<Notification, UUID> {
}
