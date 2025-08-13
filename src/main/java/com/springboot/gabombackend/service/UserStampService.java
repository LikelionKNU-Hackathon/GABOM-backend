package com.springboot.gabombackend.service;

import com.springboot.gabombackend.dto.UserStampResponse;
import com.springboot.gabombackend.repository.UserStampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStampService {

    private final UserStampRepository userStampRepository;

    public List<UserStampResponse> getUserStamps(Long userId) {
        return userStampRepository.findUserStampsWithSum(userId);
    }
}
