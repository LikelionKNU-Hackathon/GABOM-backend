package com.springboot.gabombackend.journal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalCaseRepository extends JpaRepository<JournalCase, Long> {
}
