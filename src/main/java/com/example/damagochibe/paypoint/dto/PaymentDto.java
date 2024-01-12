package com.example.damagochibe.paypoint.dto;

import com.example.damagochibe.paypoint.entity.Payment;
import com.example.damagochibe.paypoint.paytype.PayType;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    @NonNull
    private Long amount;
    @NonNull
    private String orderName;
    @NonNull
    private String orderId;

    private String yourSuccessUrl;
    private String yourFailUrl;


    public Payment toEntity() {
        return Payment.builder()
                .payType(PayType.CARD)
                .amount(amount)
                .orderName(orderName)
                .orderId(orderId)
                .paySuccessYN(false)
                .build();
    }
}
