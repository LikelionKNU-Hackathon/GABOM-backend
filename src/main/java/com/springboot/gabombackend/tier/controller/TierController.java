package com.springboot.gabombackend.tier.controller;

import com.springboot.gabombackend.tier.service.TierService;
import com.springboot.gabombackend.tier.dto.TierResponse;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.service.UserService;
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
        String loginId = authentication.getName(); // principal = loginId
        User user = userService.getByLoginId(loginId); // DB 조회
        return tierService.getUserTiers(user);
    }
}
