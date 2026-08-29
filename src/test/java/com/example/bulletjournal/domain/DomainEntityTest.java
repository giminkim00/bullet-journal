package com.example.bulletjournal.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainEntityTest {

    @Test
    void monthlyLogCalculatesItsPeriodAndConnectsToMember() {
        Member member = new Member();

        Log log = Log.createMonthly(member, YearMonth.of(2026, 8));

        assertAll(
                () -> assertSame(member, log.getMember()),
                () -> assertEquals(LogType.MONTHLY, log.getType()),
                () -> assertEquals(LocalDate.of(2026, 8, 1), log.getPeriodStart()),
                () -> assertEquals(LocalDate.of(2026, 8, 31), log.getPeriodEnd()),
                () -> assertTrue(member.getLogs().contains(log))
        );
    }

    @Test
    void monthlyLogRequiresMemberAndMonth() {
        Member member = new Member();

        assertAll(
                () -> assertThrows(
                        NullPointerException.class,
                        () -> Log.createMonthly(null, YearMonth.of(2026, 8))
                ),
                () -> assertThrows(
                        NullPointerException.class,
                        () -> Log.createMonthly(member, null)
                )
        );
    }

    @Test
    void bulletRequiresItsMandatoryValuesAndConnectsToLog() {
        Log log = Log.createMonthly(new Member(), YearMonth.of(2026, 8));

        Bullet bullet = new Bullet(
                log,
                "Review August goals",
                BulletType.TASK,
                BulletStatus.OPEN,
                BulletOriginType.MANUAL
        );

        assertAll(
                () -> assertSame(log, bullet.getLog()),
                () -> assertTrue(log.getBullets().contains(bullet))
        );
    }

    @Test
    void bulletRejectsMissingMandatoryValues() {
        Log log = Log.createMonthly(new Member(), YearMonth.of(2026, 8));

        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> new Bullet(
                                log,
                                " ",
                                BulletType.TASK,
                                BulletStatus.OPEN,
                                BulletOriginType.MANUAL
                        )
                ),
                () -> assertThrows(
                        NullPointerException.class,
                        () -> new Bullet(null, "content", BulletType.TASK,
                                BulletStatus.OPEN, BulletOriginType.MANUAL)
                ),
                () -> assertThrows(
                        NullPointerException.class,
                        () -> new Bullet(log, "content", null,
                                BulletStatus.OPEN, BulletOriginType.MANUAL)
                ),
                () -> assertThrows(
                        NullPointerException.class,
                        () -> new Bullet(log, "content", BulletType.TASK,
                                null, BulletOriginType.MANUAL)
                ),
                () -> assertThrows(
                        NullPointerException.class,
                        () -> new Bullet(log, "content", BulletType.TASK,
                                BulletStatus.OPEN, null)
                )
        );
    }
}
