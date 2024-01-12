package com.example.damagochibe.paypoint.service;

import com.example.damagochibe.paypoint.config.TossPaymentConfig;
import com.example.damagochibe.exception.CustomLogicException;
import com.example.damagochibe.exception.ExceptionCode;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.member.service.MemberService;
import com.example.damagochibe.paypoint.dto.PaymentSuccessDto;
import com.example.damagochibe.paypoint.entity.Payment;
import com.example.damagochibe.paypoint.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final MemberService memberService;
    private final TossPaymentConfig tossPaymentConfig;
    public Payment requestTossPayment(Payment payment, String userEmail) {
        Member member = memberService.findByMemberPlayerId(userEmail); //추후 멤버플레이어아이디로 변경해야함
        if (payment.getAmount() < 1000) {
            throw new CustomLogicException(ExceptionCode.INVALID_PAYMENT_AMOUNT);
        }
        payment.setCustomer(member);
        return paymentRepository.save(payment);
    }
    @Transactional
    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) throws JSONException {
        log.info("tossPaymentSuccess Call");
        Payment payment = verifyPayment(orderId, amount);
        PaymentSuccessDto result = requestPaymentAccept(paymentKey, orderId, amount);
        payment.setPaymentKey(paymentKey);//추후 결제 취소 / 결제 조회
        payment.setPaySuccessYN(true);
        payment.getCustomer().setPoint((payment.getCustomer().getPoint() + amount.intValue()));
//        memberService.updateMemberCache(payment.getCustomer()); 이부분 실제로 member의 포인트가 변경되는지 추후확인해야함.
        return result;
    }

    public Payment verifyPayment(String orderId, Long amount) {
        System.out.println("orderId :"+ orderId);
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> {
            throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
        });
        if (!payment.getAmount().equals(amount)) {
            throw new CustomLogicException(ExceptionCode.PAYMENT_AMOUNT_EXP);
        }
        return payment;
    }
    @Transactional
    public PaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
//        JSONObject params = new JSONObject();//키/값 쌍을 문자열이 아닌 오브젝트로 보낼 수 있음
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("amount", amount);
        params.put("paymentKey",paymentKey);

        PaymentSuccessDto result = null;
        try { //post요청 (url , HTTP객체 ,응답 Dto)
            System.out.println("---");
            result = restTemplate.postForObject(TossPaymentConfig.URL,
                    new HttpEntity<>(params, headers),
                    PaymentSuccessDto.class);
            System.out.println("11111111111");
        } catch (Exception e) {
            throw new CustomLogicException(ExceptionCode.ALREADY_APPROVED);
        }

        return result;
    }
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(
                Base64.getEncoder().encodeToString((tossPaymentConfig.getTestSecretKey() + ":").getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
    @Transactional
    public void tossPaymentFail(String code, String message, String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> {
            throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
        });
        payment.setPaySuccessYN(false);
        payment.setFailReason(message);
    }

}
