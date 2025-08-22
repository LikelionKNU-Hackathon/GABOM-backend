package com.springboot.gabombackend.journal;

import com.springboot.gabombackend.journal.dto.JournalCaseUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/journal")
public class JournalCaseController {

    private final JournalCaseService journalCaseService;

    @GetMapping("/cases")
    public List<Map<String, Object>> getCases(@AuthenticationPrincipal Long userId) {
        return journalCaseService.getAllCasesWithSelection(userId);
    }

    @PatchMapping("/case")
    public Map<String, Object> updateCase(@AuthenticationPrincipal Long userId,
                                          @RequestBody JournalCaseUpdateRequest request) {
        journalCaseService.updateUserCase(userId, request.getCaseId());
        return Map.of(
                "message", "케이스가 변경되었습니다.",
                "caseId", request.getCaseId()
        );
    }

}