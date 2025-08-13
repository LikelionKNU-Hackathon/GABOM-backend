package com.springboot.gabombackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "titles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 스탬프 카테고리 (예: 일식, 한식, 카페 등)
    @Column(nullable = false, length = 50)
    private String category;

    // 칭호 이름
    @Column(nullable = false, length = 100)
    private String name;

    // 칭호 설명
    @Column(length = 255)
    private String description;

    // 목표 방문 횟수
    @Column(nullable = false, name = "goal_count")
    private int goalCount;
}
