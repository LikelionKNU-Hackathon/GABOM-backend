package com.springboot.gabombackend.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_stamps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id", nullable = false)
    private Stamp stamp;

    @Column(nullable = false)
    private int count;
}
