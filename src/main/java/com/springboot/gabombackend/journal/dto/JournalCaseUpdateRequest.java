package com.springboot.gabombackend.journal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JournalCaseUpdateRequest {
    private Long caseId; // 변경할 케이스 ID
}
