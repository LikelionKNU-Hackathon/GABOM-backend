package com.springboot.gabombackend.repository;

import com.springboot.gabombackend.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    List<Stamp> findByCategory(String category);
}
