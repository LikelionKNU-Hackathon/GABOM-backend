package com.springboot.gabombackend.reward.repository;

import com.springboot.gabombackend.reward.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward, Long> {
}
