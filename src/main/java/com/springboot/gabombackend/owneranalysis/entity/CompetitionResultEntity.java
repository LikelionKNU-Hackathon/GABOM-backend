package com.springboot.gabombackend.owneranalysis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "owner_analysis_competition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storeId;

    // 카테고리 ID 대신 이름(String) 저장
    @Column(nullable = false, length = 50)
    private String categoryName;

    // 내 가게 점수
    private Integer myPrice;     // 가성비
    private Integer myQuantity;  // 양
    private Integer myTaste;     // 맛
    private Integer myService;   // 서비스
    private Integer myClean;     // 위생

    // 경쟁 가게 점수
    private Integer compPrice;
    private Integer compQuantity;
    private Integer compTaste;
    private Integer compService;
    private Integer compClean;

    private LocalDateTime analyzedAt;
}
