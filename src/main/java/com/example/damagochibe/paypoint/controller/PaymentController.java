package com.example.damagochibe.paypoint.controller;

import com.example.damagochibe.auth.security.CustomUserDetail;
import com.example.damagochibe.paypoint.config.TossPaymentConfig;
import com.example.damagochibe.paypoint.dto.PaymentReqDto;
import com.example.damagochibe.paypoint.service.PaymentService;
import com.example.damagochibe.paypoint.dto.PaymentDto;
import com.example.damagochibe.paypoint.dto.PaymentFailDto;
import com.example.damagochibe.paypoint.dto.PaymentResDto;
import com.example.damagochibe.response.SingleResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequestMapping("payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final TossPaymentConfig tossPaymentConfig;
    public PaymentController(PaymentService paymentService, TossPaymentConfig tossPaymentConfig) {
        this.paymentService = paymentService;
        this.tossPaymentConfig = tossPaymentConfig;
    }
    @PostMapping("/toss")
    public ResponseEntity requestTossPayment(@AuthenticationPrincipal CustomUserDetail principal, @RequestBody @Valid PaymentDto paymentReqDto) {
        System.out.println("principal = " + principal.getEmail());
        PaymentResDto paymentResDto = paymentService.requestTossPayment(paymentReqDto.toEntity(), principal.getUsername()).toPaymentResDto();
        paymentResDto.setSuccessUrl(paymentReqDto.getYourSuccessUrl() == null ? tossPaymentConfig.getSuccessUrl() : paymentReqDto.getYourSuccessUrl());
        paymentResDto.setFailUrl(paymentReqDto.getYourFailUrl() == null ? tossPaymentConfig.getFailUrl() : paymentReqDto.getYourFailUrl());
        return ResponseEntity.ok(paymentResDto);
    }
    @PostMapping("/toss/success")
    public ResponseEntity tossPaymentSuccess(
            @RequestBody PaymentReqDto dto
            ) throws JSONException {
        String paymentKey = dto.getPaymentKey();
        String orderId = dto.getOrderId();
        Long amount = dto.getAmount();
        System.out.println("orderId :"+orderId);
        return ResponseEntity.ok().body(paymentService.tossPaymentSuccess(paymentKey,orderId,amount));
    }
    @GetMapping("/toss/fail")
    public ResponseEntity tossPaymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId
    ) {
        paymentService.tossPaymentFail(code, message, orderId);
        return ResponseEntity.ok().body(new SingleResponse<>(
                PaymentFailDto.builder()
                        .errorCode(code)
                        .errorMessage(message)
                        .orderId(orderId)
                        .build()
        ));
    }
}
