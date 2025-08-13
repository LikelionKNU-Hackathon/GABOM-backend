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
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, name = "image_url")
    private String imageUrl;
}
