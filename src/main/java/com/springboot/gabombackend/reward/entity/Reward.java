package com.springboot.gabombackend.reward.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rewardName; // ex. 온누리 상품권 5,000원

    @Column(nullable = false)
    private int stampNeeded;   // ex. 50, 100, 250
}