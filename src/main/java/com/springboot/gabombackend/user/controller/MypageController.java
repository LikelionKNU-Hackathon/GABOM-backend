package com.springboot.gabombackend.user.controller;

import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.service.UserService;
import com.springboot.gabombackend.user.dto.UserResponseDto;
import com.springboot.gabombackend.user.dto.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MypageController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyPage(Authentication authentication) {
        String loginId = authentication.getName();
        User user = userService.getByLoginId(loginId);
        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDto> updateMyPage(
            Authentication authentication,
            @RequestBody UserUpdateDto updateDto) {

        String loginId = authentication.getName();
        User updatedUser = userService.updateUser(loginId, updateDto);
        return ResponseEntity.ok(new UserResponseDto(updatedUser));
    }
}
