package com.example.damagochibe.paypoint.entity;

import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.paypoint.dto.PaymentResDto;
import com.example.damagochibe.paypoint.paytype.PayType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long paymentId;
    @Column(nullable = false, name = "pay_type")
    @Enumerated(EnumType.STRING)
    private PayType payType;
    @Column(nullable = false, name = "pay_amount")
    private Long amount;
    @Column(nullable = false, name = "pay_name")
    private String orderName;
    @Column(nullable = false, name = "order_id")
    private String orderId;

    private boolean paySuccessYN;
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member customer;
    @Column
    private String paymentKey;
    @Column
    private String failReason;

    @Column
    private boolean cancelYN;
    @Column
    private String cancelReason;

    public PaymentResDto toPaymentResDto() {
        PaymentResDto resDto = new PaymentResDto();

        resDto.setPayType(this.payType.name()); // Enum의 이름을 String으로 변환
        resDto.setAmount(this.amount);
        resDto.setOrderName(this.orderName);
        resDto.setOrderId(this.orderId);

        // customer가 null이 아닌 경우에만 값을 설정하도록 합니다.
        if (this.customer != null) {
            resDto.setCustomerEmail(this.customer.getPlayerId());
            resDto.setCustomerName(this.customer.getPlayerId());
        }

        // Success와 Fail URL은 Payment 엔티티에 없으므로, 이 값들은 다른 로직에서 설정해야 합니다.
        // resDto.setSuccessUrl(...);
        // resDto.setFailUrl(...);

        resDto.setFailReason(this.failReason);
        resDto.setCancelYN(this.cancelYN);
        resDto.setCancelReason(this.cancelReason);

        // createdAt은 Payment 엔티티에 없습니다. 만약 createdAt이 있고 LocalDateTime 타입이라면 다음과 같이 설정할 수 있습니다.
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // resDto.setCreatedAt(this.createdAt.format(formatter));

        // 현재 Payment 엔티티에는 생성 시간(createdAt)에 해당하는 필드가 없습니다.
        // 해당 정보가 필요하다면, Payment 엔티티에 해당 필드를 추가하고 포매팅하여 설정해야 합니다.

        return resDto;
    }
}
