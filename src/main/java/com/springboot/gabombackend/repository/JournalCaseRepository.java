package com.springboot.gabombackend.repository;

import com.springboot.gabombackend.entity.JournalCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalCaseRepository extends JpaRepository<JournalCase, Long> {
}
