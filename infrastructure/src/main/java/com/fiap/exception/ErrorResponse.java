package com.fiap.exception;

import java.time.Instant;

public class ErrorResponse {

    private Instant timestamp;
    private String code;
    private String message;

    public ErrorResponse(Instant timestamp, String code, String message) {
        this.timestamp = timestamp;
        this.code = code;
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
