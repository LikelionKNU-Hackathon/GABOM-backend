package com.springboot.gabombackend.journal.controller;

import com.springboot.gabombackend.journal.service.JournalCaseService;
import com.springboot.gabombackend.journal.dto.JournalCaseUpdateRequest;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/journal")
public class JournalCaseController {

    private final JournalCaseService journalCaseService;
    private final UserService userService;

    @GetMapping("/cases")
    public List<Map<String, Object>> getCases(Authentication authentication) {
        String loginId = authentication.getName();
        User user = userService.getByLoginId(loginId);
        return journalCaseService.getAllCasesWithSelection(user.getId());
    }

    @PatchMapping("/case")
    public Map<String, Object> updateCase(Authentication authentication,
                                          @RequestBody JournalCaseUpdateRequest request) {
        String loginId = authentication.getName();
        User user = userService.getByLoginId(loginId);
        journalCaseService.updateUserCase(user.getId(), request.getCaseId());
        return Map.of(
                "message", "케이스가 변경되었습니다.",
                "caseId", request.getCaseId()
        );
    }
}
