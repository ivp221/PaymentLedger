package com.IshaVishakha.PaymentLedger.Controller;

import com.IshaVishakha.PaymentLedger.Dto.CreateTransactionRequest;
import com.IshaVishakha.PaymentLedger.Dto.TransactionResponse;
import com.IshaVishakha.PaymentLedger.Service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/Transaction")
@RequiredArgsConstructor
public class PaymentController {
    private final TransactionService transactionService;

    @PostMapping("/submitTransaction")
    public ResponseEntity<TransactionResponse> submitTransaction(@Valid @RequestBody CreateTransactionRequest request, @RequestHeader(value = "idempotency-key")String idempotencyKey){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(transactionService.submitTransaction(request, idempotencyKey));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable UUID transactionId){
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransaction(transactionId));

    }
    @GetMapping
    public Page<TransactionResponse> listTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "createdAt"));
        return transactionService.listTransactions(pageable);
    }



}
