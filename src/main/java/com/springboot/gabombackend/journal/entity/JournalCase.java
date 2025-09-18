package com.springboot.gabombackend.journal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "journal_cases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name; // 케이스 이름

    @Column(nullable = false, length = 20)
    private String category; // 벚꽃, 여름, 동물, 패턴

    @Column(nullable = false, length = 255)
    private String imageUrl; // S3 이미지 경로
}
