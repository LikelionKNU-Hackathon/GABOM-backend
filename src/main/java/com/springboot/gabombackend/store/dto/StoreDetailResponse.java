package com.springboot.gabombackend.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StoreDetailResponse {
    private Long id;
    private String name;
    private String category;
    private String openingHours;
    private String address;

    private VisitorInfo topVisitor;
    private VisitorInfo myVisit;

    @Data
    @Builder
    @AllArgsConstructor
    public static class VisitorInfo {
        private String nickname;
        private int visitCount;
    }
}