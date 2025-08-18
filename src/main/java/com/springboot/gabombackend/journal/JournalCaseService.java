package com.springboot.gabombackend.journal;

import com.springboot.gabombackend.user.User;
import com.springboot.gabombackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JournalCaseService {

    private final JournalCaseRepository journalCaseRepository;
    private final UserJournalCaseRepository userJournalCaseRepository;
    private final UserRepository userRepository;

    public List<Map<String, Object>> getAllCasesWithSelection(Long userId) {
        Long selectedCaseId = userJournalCaseRepository.findByUserId(userId)
                .map(uc -> uc.getJournalCase().getId())
                .orElse(null);

        return journalCaseRepository.findAll().stream()
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", c.getId());
                    map.put("name", c.getName());
                    map.put("category", c.getCategory());
                    map.put("imageUrl", c.getImageUrl());
                    map.put("selected", Objects.equals(c.getId(), selectedCaseId));
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserCase(Long userId, Long caseId) {
        JournalCase journalCase = journalCaseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 케이스가 존재하지 않습니다."));

        UserJournalCase userJournalCase = userJournalCaseRepository.findByUserId(userId)
                .orElse(null);

        if (userJournalCase == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
            userJournalCase = UserJournalCase.builder()
                    .user(user)
                    .journalCase(journalCase)
                    .build();
        } else {
            userJournalCase.setJournalCase(journalCase);
        }

        userJournalCaseRepository.save(userJournalCase);
    }

}
