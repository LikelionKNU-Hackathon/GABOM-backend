package com.springboot.gabombackend.owneranalysis.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionAnalysisDto {
    private Long storeId;
    private String storeName;   // 가게 이름
    private String category;    // 카테고리명

    // 내 가게 점수
    private Integer myPrice;
    private Integer myQuantity;
    private Integer myTaste;
    private Integer myService;
    private Integer myClean;

    // 경쟁 가게 점수
    private Integer compPrice;
    private Integer compQuantity;
    private Integer compTaste;
    private Integer compService;
    private Integer compClean;

    private LocalDateTime analyzedAt;
}
