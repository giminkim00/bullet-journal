package com.example.bulletjournal.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "member")
    private List<Log> logs = new ArrayList<>();

    protected Member() {
    }

    public Member(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("회원 이름은 필수입니다.");
        }

        this.name = name.trim();
    }

    public List<Log> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    void addLog(Log log) {
        Objects.requireNonNull(log, "log must not be null");
        if (log.getMember() != this) {
            throw new IllegalArgumentException("log must belong to this member");
        }
        logs.add(log);
    }
}
