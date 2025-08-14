package com.springboot.gabombackend.repository;

import com.springboot.gabombackend.entity.UserJournalCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJournalCaseRepository extends JpaRepository<UserJournalCase, Long> {
    Optional<UserJournalCase> findByUserId(Long userId);
}
