package com.IshaVishakha.PaymentLedger.Service;

import com.IshaVishakha.PaymentLedger.Configuration.TransactionProperties;
import com.IshaVishakha.PaymentLedger.Dto.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyServiceImpl implements IdempotencyService {
    private static final String KEY_PREFIX = "idempotency:payment:";
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final TransactionProperties transactionProperties;
    @Override
    public Optional<TransactionResponse> getCachedResponse(String idempotencyKey) {
        String json = stringRedisTemplate.opsForValue().get(KEY_PREFIX+idempotencyKey);
        if(json == null) return Optional.empty();
        try {
            return Optional.of(objectMapper.readValue(json, TransactionResponse.class));
        } catch (Exception e) {
            log.warn("Failed to deserialize the cached idempotent response for key {}", idempotencyKey);
            return Optional.empty();
        }
    }

    @Override
    public void cacheResponse(String idempotencyKey, TransactionResponse response) {
        try {
            String json= objectMapper.writeValueAsString(response);
            stringRedisTemplate.opsForValue().set(KEY_PREFIX+idempotencyKey , json, Duration.ofHours(transactionProperties.getIdempotencyTtlHours()));
        } catch (Exception e) {
            log.warn("Failed to cache idempotent response for key {}" , idempotencyKey);
        }

    }
}
