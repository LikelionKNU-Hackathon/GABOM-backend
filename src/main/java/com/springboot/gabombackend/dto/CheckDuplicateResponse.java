package com.springboot.gabombackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckDuplicateResponse {
    private String type;  // 아이디 | 이메일 | 닉네임
    private String value;  //검사한 값
    private boolean duplicate;
}
