package com.example.damagochibe.exception;

public class CustomLogicException extends RuntimeException {
    private final ExceptionCode code;

    public CustomLogicException(ExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public CustomLogicException(ExceptionCode code, String message) {
        super(message);
        this.code = code;
    }

    public ExceptionCode getCode() {
        return code;
    }
}

