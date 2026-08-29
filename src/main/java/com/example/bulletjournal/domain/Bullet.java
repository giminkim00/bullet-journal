package com.example.bulletjournal.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
public class Bullet {

    @Id
    @GeneratedValue
    @Column(name = "bullet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "log_id", nullable = false)
    private Log log;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_bullet_id")
    private Bullet bullet;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BulletType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BulletStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BulletOriginType originType;

    private Integer targetMonth;
    private Integer targetDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Bullet() {
    }

    public Bullet(Log log, String content, BulletType type,
                  BulletStatus status, BulletOriginType originType) {
        this.log = Objects.requireNonNull(log, "log must not be null");
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("content must not be blank");
        }
        this.content = content;
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.originType = Objects.requireNonNull(originType, "originType must not be null");
        log.addBullet(this);
    }

}
