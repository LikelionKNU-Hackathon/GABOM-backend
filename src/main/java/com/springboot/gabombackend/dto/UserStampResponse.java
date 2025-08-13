package com.springboot.gabombackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserStampResponse {
    private Long stampId;
    private String name;
    private String category;
    private String imageUrl;
    private Long count;
}
