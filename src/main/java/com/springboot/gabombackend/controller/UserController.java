package com.springboot.gabombackend.controller;

import com.springboot.gabombackend.dto.CheckDuplicateResponse;
import com.springboot.gabombackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/check")
    public CheckDuplicateResponse checkDuplicate(@RequestParam String type,@RequestParam String value) {

        boolean duplicate = false;

        if ("loginId".equals(type)) {
            duplicate = userRepository.existsByLoginId(value);
        } else if ("email".equals(type)) {
            duplicate = userRepository.existsByEmail(value);
        } else if ("nickname".equals(type)) {
            duplicate = userRepository.existsByNickname(value);
        } else {
            throw new IllegalArgumentException("잘못된 type 값입니다.");
        }

        return CheckDuplicateResponse.builder()
                .type(type)
                .value(value)
                .duplicate(duplicate)
                .build();
    }
}

