package com.springboot.gabombackend.tier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TierResponse {
    private String currentTier; // 현재 티어
    private List<TierProgress> tierProgress; // 진행 상태
}
