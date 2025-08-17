package com.springboot.gabombackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stamps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;  // 스탬프 이름 (예: asia_1, bunsik_2)

    @Column(nullable = false)
    private String category;  // 스탬프 카테고리 (예: 분식, 한식, 중식, 카페)

    @Column(nullable = false, name = "image_url")
    private String imageUrl;  // 스탬프 이미지 경로
}
