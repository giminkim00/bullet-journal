package com.example.bulletjournal.service;

import com.example.bulletjournal.domain.Log;
import com.example.bulletjournal.domain.LogType;
import com.example.bulletjournal.domain.Member;
import com.example.bulletjournal.repository.LogRepository;
import com.example.bulletjournal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createMonthlyLog(Long memberId, YearMonth month) {
        Member member = findMember(memberId);

        LocalDate periodStart = month.atDay(1);
        LocalDate periodEnd = month.atEndOfMonth();

        validateDuplicateLog(memberId, LogType.MONTHLY, periodStart, periodEnd);

        Log log = Log.createMonthly(member, month);
        logRepository.save(log);

        return log.getId();
    }

    public Log findOne(Long memberId, Long logId) {
        return logRepository.findOne(memberId, logId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Log를 찾을 수 없습니다: " + logId
                        )
                );
    }

    public List<Log> findLogs(Long memberId) {
        findMember(memberId);
        return logRepository.findAllByMemberId(memberId);

    }

    public Member findMember(Long memberId) {
        return memberRepository.findOne(memberId)
                .orElseThrow(() ->
                        new IllegalArgumentException("회원을 찾을 수 없습니다. " + memberId
                        )
                );
    }

    private void validateDuplicateLog(
            Long memberId,
            LogType type,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {
        if (logRepository.exists(
                memberId,
                type,
                periodStart,
                periodEnd
        )) {
            throw new IllegalStateException(
                    "해당 기간의 Log가 이미 존재합니다."
            );
        }
    }
}
