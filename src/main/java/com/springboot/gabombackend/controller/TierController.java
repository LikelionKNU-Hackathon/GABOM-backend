package com.springboot.gabombackend.controller;

import com.springboot.gabombackend.dto.TierResponse;
import com.springboot.gabombackend.service.TierService;
import com.springboot.gabombackend.entity.User;
import com.springboot.gabombackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/tiers")
public class TierController {

    private final TierService tierService;
    private final UserService userService;

    @GetMapping
    public TierResponse getMyTiers(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        User user = userService.getById(userId); // DB 조회
        return tierService.getUserTiers(user);
    }
}
