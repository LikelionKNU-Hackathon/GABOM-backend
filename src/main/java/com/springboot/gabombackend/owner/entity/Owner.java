package com.springboot.gabombackend.owner.entity;

import com.springboot.gabombackend.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "owners")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    // 사업자등록 관련 필드
    @Column(length = 20)
    private String businessNumber; // 사업자등록번호

    @Column(length = 50)
    private String representativeName; // 대표자명

    @Column(length = 8)
    private String openDate; // 개업일자 (YYYYMMDD)

    private boolean verified; // 국세청 인증 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
