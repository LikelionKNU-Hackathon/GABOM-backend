package com.springboot.gabombackend.user.dto;

import com.springboot.gabombackend.user.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private String username;
    private String loginId;
    private String email;
    private String nickname;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
