package com.springboot.gabombackend.title.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTitleResponse {
    private Long titleId;
    private String name;
    private String description;
    private int currentCount;
    private int goalCount;
    private boolean achieved;
    private boolean representative;
}
