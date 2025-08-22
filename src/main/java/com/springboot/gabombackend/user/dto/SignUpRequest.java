package com.springboot.gabombackend.user.dto;

import com.springboot.gabombackend.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String username; // DTO에서 입력받는 사용자 이름
    private String loginId;
    private String password;
    private String email;
    private String nickname;

    public User toEntity(String encodedPw) {
        return User.builder()
                .username(this.username)
                .loginId(this.loginId)
                .password(encodedPw)
                .email(this.email)
                .nickname(this.nickname)
                .build();
    }
}
