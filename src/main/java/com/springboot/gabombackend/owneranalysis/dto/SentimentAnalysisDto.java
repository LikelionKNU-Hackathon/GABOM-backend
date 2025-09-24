package com.springboot.gabombackend.owneranalysis.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisDto {
    private Long storeId;
    private String storeName;   // 가게 이름
    private Integer reviewCount; // 총 리뷰 개수
    private Integer positive;    // 긍정 %
    private Integer negative;    // 부정 %
    private LocalDateTime analyzedAt;
}