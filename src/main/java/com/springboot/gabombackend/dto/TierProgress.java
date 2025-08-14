package com.springboot.gabombackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TierProgress {
    private String name;     // 티어 이름
    private String emoji;    // 이모지
    private int current;     // 현재 스탬프 개수
    private int goal;        // 목표 개수
    private boolean achieved; // 달성 여부
}
