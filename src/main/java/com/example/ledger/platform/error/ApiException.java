package com.example.ledger.platform.error;

/** Business exception carrying a stable error code. Thrown by services, rendered by GlobalExceptionHandler. */
public class ApiException extends RuntimeException {
    private final ErrorCode code;

    public ApiException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCode code() { return code; }
}
