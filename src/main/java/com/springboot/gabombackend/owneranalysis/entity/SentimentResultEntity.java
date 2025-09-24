package com.springboot.gabombackend.owneranalysis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "owner_analysis_sentiment")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SentimentResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storeId;

    private Integer reviewCount;  // 총 리뷰 개수
    private Integer positive;     // 긍정 %
    private Integer negative;     // 부정 %

    private LocalDateTime analyzedAt;
}
