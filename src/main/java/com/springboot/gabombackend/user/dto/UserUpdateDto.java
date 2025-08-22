package com.springboot.gabombackend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    private String username;
    private String password;
    private String email;
    private String nickname;
}
