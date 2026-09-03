package com.example.bulletjournal.service;

import com.example.bulletjournal.domain.Log;
import com.example.bulletjournal.domain.LogType;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;

@Slf4j
@SpringBootTest
@Transactional
public class LogServiceTest {

    @Autowired
    LogService logService;
    @Autowired
    MemberService memberService;

    @Test
    public void 같은_월_로그_방지() throws Exception {
        //given
        Long memberId = memberService.join("kim");
        YearMonth month = YearMonth.of(2026, 8);

        logService.createMonthlyLog(memberId, month);

        //when

        //then
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> logService.createMonthlyLog(memberId, month)
        );
    }

    @Test
    public void 월간_로그의_시작일과_종료일() throws Exception {
        //given
        Long memberId = memberService.join("kim");
        YearMonth month = YearMonth.of(2026, 8);

        //when
        Long logId = logService.createMonthlyLog(memberId, month);
        Log log = logService.findOne(memberId, logId);

        //then
        org.assertj.core.api.Assertions.assertThat(log.getType()).isEqualTo(LogType.MONTHLY);

        org.assertj.core.api.Assertions.assertThat(log.getPeriodStart()).isEqualTo(LocalDate.of(2026, 8, 1));
        org.assertj.core.api.Assertions.assertThat(log.getPeriodEnd()).isEqualTo(LocalDate.of(2026, 8, 31));
    }
    
    @Test
    public void 다른_회원_로그조회() throws Exception {
        //given
        Long memberId = memberService.join("kim");
        Long memberId2 = memberService.join("lee");

        YearMonth month = YearMonth.of(2026, 8);

        //when
        Long logId = logService.createMonthlyLog(memberId, month);

        //then
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> logService.findOne(memberId2, logId)
        );

        org.assertj.core.api.Assertions.assertThat(logId).isEqualTo(logService.findLogs(memberId).get(0).getId());
    
    }
}
