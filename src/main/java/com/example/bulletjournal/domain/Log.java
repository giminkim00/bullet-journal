package com.example.bulletjournal.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Log {

    @Id
    @GeneratedValue
    @Column(name = "log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LogType type;
    private int periodStart;
    private int periodEnd;
}
