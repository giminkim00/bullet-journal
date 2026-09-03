package com.example.bulletjournal.service;

import com.example.bulletjournal.domain.Member;
import com.example.bulletjournal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(String name) {
        Member member = new Member(name);

        validateDuplicateMember(member.getName());

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(String name) {
        if (memberRepository.existsByName(name)) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId)
                .orElseThrow(() ->
                        new IllegalArgumentException("회원을 찾을 수 없습니다. " + memberId)
                );
    }

}
