package com.example.bulletjournal.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
public class Log {

    @Id
    @GeneratedValue
    @Column(name = "log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL)
    private List<Bullet> bullets = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogType type;

    @Column(nullable = false)
    private LocalDate periodStart;

    @Column(nullable = false)
    private LocalDate periodEnd;

    protected Log() {
    }

    private Log(Member member, LogType type, LocalDate periodStart, LocalDate periodEnd) {
        this.member = Objects.requireNonNull(member, "member must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.periodStart = Objects.requireNonNull(periodStart, "periodStart must not be null");
        this.periodEnd = Objects.requireNonNull(periodEnd, "periodEnd must not be null");
        member.addLog(this);
    }

    public static Log createMonthly(Member member, YearMonth month) {
        Objects.requireNonNull(month, "month must not be null");
        return new Log(member, LogType.MONTHLY, month.atDay(1), month.atEndOfMonth());
    }

    public List<Bullet> getBullets() {
        return Collections.unmodifiableList(bullets);
    }

    void addBullet(Bullet bullet) {
        Objects.requireNonNull(bullet, "bullet must not be null");
        if (bullet.getLog() != this) {
            throw new IllegalArgumentException("bullet must belong to this log");
        }
        bullets.add(bullet);
    }

}
