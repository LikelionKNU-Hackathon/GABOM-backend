package com.springboot.gabombackend.reward.repository;

import com.springboot.gabombackend.reward.entity.UserReward;
import com.springboot.gabombackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRewardRepository extends JpaRepository<UserReward, Long> {
    List<UserReward> findByUser(User user);
}
