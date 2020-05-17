package com.ebay.salesstatsservice.model;

import java.time.OffsetDateTime;

public class ErrorResponse {

    private OffsetDateTime time = OffsetDateTime.now();
    private String message;
    private String stackTrace;

    public ErrorResponse(String message, String stackTrace) {
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}
