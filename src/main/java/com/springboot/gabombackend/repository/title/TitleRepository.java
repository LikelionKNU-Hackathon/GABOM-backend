package com.springboot.gabombackend.repository.title;

import com.springboot.gabombackend.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TitleRepository extends JpaRepository<Title, Long> {
    Optional<Title> findByCategory(String category);
}