package com.example.damagochibe.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@DynamicInsert
@Entity
@Table(name = "auth")
public class Auth implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long authId;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "role")
    private String role;

    @Override
    public String getAuthority() {
        return role;
    }
}
