package com.springboot.gabombackend.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OwnerResponse {
    private String loginId;   // 업주 로그인 아이디
    private String storeName; // 업장 이름
    private Long storeId;     // 업장 ID
}