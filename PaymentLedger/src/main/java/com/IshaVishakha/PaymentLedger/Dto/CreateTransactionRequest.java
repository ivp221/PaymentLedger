package com.IshaVishakha.PaymentLedger.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateTransactionRequest {
    @NotNull UUID senderAccountId;
    @NotNull UUID receiverAccountId;
    @NotNull @DecimalMin(value = "0.01", message ="amount cannot be less than 0.01") BigDecimal amount;
    @NotNull @Pattern(regexp = "[A-Z]{3}", message = "Currency must be a 3 letter ISO code") String currency;
}
