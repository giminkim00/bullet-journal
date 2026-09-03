package com.example.bulletjournal.service;

import com.example.bulletjournal.domain.Member;
import com.example.bulletjournal.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;

    @Test
    public void 회원가입() throws Exception {
        Long savedId = memberService.join("kim");

        em.flush();
        em.clear();

        Member foundMember = memberService.findOne(savedId);

        assertThat(foundMember.getId()).isEqualTo(savedId);
        assertThat(foundMember.getName()).isEqualTo("kim");
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        memberService.join("kim");

        assertThrows(
                IllegalStateException.class,
                () -> memberService.join("kim")
        );
    }

    @Test
    public void 빈_이름은_가입할_수_없다() throws Exception {
        //given

        //when
        assertThrows(
                IllegalArgumentException.class,
                () -> memberService.join(" ")
        );
        //then

    }
}
