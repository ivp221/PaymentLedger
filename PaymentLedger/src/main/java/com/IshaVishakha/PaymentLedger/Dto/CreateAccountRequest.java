package com.IshaVishakha.PaymentLedger.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;




@Data
public class CreateAccountRequest {
    @NotBlank String accountHolderName;
    @NotBlank @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3 letter ISO code") String currency;

}
