package com.example.bulletjournal.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Bullet {

    @Id
    @GeneratedValue
    @Column(name = "bullet_id")
    private Long id;

    @JoinColumn(name = "log_id")
    private Log log;

    @JoinColumn(name = "source_bullet_id")
    private Bullet bullet;

    private String content;
    private BulletType type;
    private BulletStatus status;
    private BulletOriginType originType;
    private Integer targetMonth;
    private Integer targetDate;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
