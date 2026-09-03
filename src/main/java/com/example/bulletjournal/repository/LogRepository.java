package com.example.bulletjournal.repository;

import com.example.bulletjournal.domain.Log;
import com.example.bulletjournal.domain.LogType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LogRepository {

    private final EntityManager em;

    public void save(Log log) {
        em.persist(log);
    }

    public Optional<Log> findOne(Long memberId, Long logId) {
        return em.createQuery(
                        """
                                select l
                                from Log l
                                where l.id = :logId
                                and l.member.id = :memberId
                                """,
                        Log.class
                )
                .setParameter("logId", logId)
                .setParameter("memberId", memberId)
                .getResultStream()
                .findFirst();
    }

    public List<Log> findAllByMemberId(Long memberId) {
        return em.createQuery(
                        """
                                select l
                                from Log l
                                where l.member.id = :memberId
                                order by l.periodStart desc
                                """,
                        Log.class
                )
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public boolean exists(
            Long memberId,
            LogType type,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {
        Long count = em.createQuery(
                        """
                                select count(l)
                                from Log l
                                where l.member.id = :memberId
                                and l.type = :type
                                and l.periodStart = :periodStart
                                and l.periodEnd = :periodEnd
                                """,
                        Long.class
                )
                .setParameter("memberId", memberId)
                .setParameter("type", type)
                .setParameter("periodStart", periodStart)
                .setParameter("periodEnd", periodEnd)
                .getSingleResult();

        return count > 0;
    }

}
