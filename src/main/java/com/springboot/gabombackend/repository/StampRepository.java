package com.springboot.gabombackend.repository;

import com.springboot.gabombackend.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    List<Stamp> findByCategory(String category);
    Optional<Stamp> findFirstByCategory(String category);

}
