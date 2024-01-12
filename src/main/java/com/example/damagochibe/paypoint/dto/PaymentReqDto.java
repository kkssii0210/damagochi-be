package com.example.damagochibe.paypoint.dto;

import lombok.Getter;

@Getter
public class PaymentReqDto {
    private String paymentKey;
    private String orderId;
    private Long amount;
}
