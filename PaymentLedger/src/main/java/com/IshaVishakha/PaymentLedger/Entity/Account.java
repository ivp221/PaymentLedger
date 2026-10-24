package com.IshaVishakha.PaymentLedger.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name ="accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "account_holder_name", nullable = false)
    private String accountHolderName;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "account_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal accountBalance;

    @CreationTimestamp
    @Column(name ="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    //debit and credit methods belong in the Wallet Entity because the entity controls how the balance changes instead of letting a service control it. (ENCAPSULATION)
    public void debit (BigDecimal amount){
        this.accountBalance = this.accountBalance.subtract(amount);
    }

    public void credit (BigDecimal amount){
        this.accountBalance = this.accountBalance.add(amount);
    }



}
