package com.springboot.gabombackend.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 가게 PK

    @Column(nullable = false, length = 100)
    private String name;  // 가게명

    @Column(nullable = false, length = 255, unique = true)
    private String address;  // 주소 (고유 제약)

    @Column(length = 50, nullable = false)
    private String category;  // 카테고리 (예: 분식, 한식, 중식, 양식, 편의시설)

    @Column(name = "opening_hours", length = 100)
    private String openingHours;  // 영업시간 (문자열 그대로 저장)

    @Column(nullable = false)
    private Double latitude;  // 위도

    @Column(nullable = false)
    private Double longitude;  // 경도
}
