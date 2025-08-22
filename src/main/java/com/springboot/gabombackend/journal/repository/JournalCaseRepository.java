package com.springboot.gabombackend.journal.repository;

import com.springboot.gabombackend.journal.entity.JournalCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalCaseRepository extends JpaRepository<JournalCase, Long> {
}
