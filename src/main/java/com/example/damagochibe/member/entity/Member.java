package com.example.damagochibe.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DynamicInsert
@JsonIgnoreProperties({"isSocialMember"})
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "player_id", nullable = false)
    @Email
    private String playerId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "point")
    private Integer point;

    @Column(name="is_social_member", columnDefinition = "boolean default false", nullable = false)
    private Boolean isSocialMember;


}
