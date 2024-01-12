package com.example.damagochibe.exception;

// 예외 코드를 나타내는 열거형
public enum ExceptionCode {
    INVALID_PAYMENT_AMOUNT("1000원보단 많이사셔야죠"),
    PAYMENT_NOT_FOUND("찾을수 없는 결제네요"),
    PAYMENT_AMOUNT_EXP("값이 안맞는데요"),
    ALREADY_APPROVED("이미 승인이 됐는데요");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
