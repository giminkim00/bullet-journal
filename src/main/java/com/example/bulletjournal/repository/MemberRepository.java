package com.example.bulletjournal.repository;

import com.example.bulletjournal.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findOne(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public boolean existsByName(String name) {
        Long count = em.createQuery(
                        "select count(m) from Member m where m.name = :name",
                        Long.class
                )
                .setParameter("name", name)
                .getSingleResult();

        return count > 0;
    }
}
