package com.example.damagochibe.management.global.cooldown.entity;

import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.monginfo.entity.Mong;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(schema = "cooldown")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Cooldown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mong_id", nullable = false, unique = true)
    private Mong mong;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean feed; // 디폴트 값을 false로 설정

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean stroke; // 디폴트 값을 false로 설정

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean training; // 디폴트 값을 false로 설정

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean sleep; // 디폴트 값을 false로 설정


}
