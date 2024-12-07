package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.dto.SleepSessionDTO;
import com.noom.interview.fullstack.sleep.entity.SleepSession;
import com.noom.interview.fullstack.sleep.util.WakeUpFeeling;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.noom.interview.fullstack.sleep.SleepApplication.UNIT_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles(UNIT_TEST_PROFILE)
public class SleepSessionMapperTest {
    @Test
    public void shouldMapSleepSessionEntityToDto(){
        SleepSession sleepSession = SleepSession
                .builder()
                .sleepDate(java.sql.Date.valueOf(LocalDate.now().minusDays(1)))
                .sleepStart(Time.valueOf(LocalTime.of(23, 0)))
                .sleepEnd(Time.valueOf(LocalTime.of(8, 0)))
                .wakeUpFeeling(WakeUpFeeling.GOOD)
                .build();

        SleepSessionDTO expectedSleepSessionDTO = SleepSessionDTO
                .builder()
                .sleepDate(java.sql.Date.valueOf(LocalDate.now().minusDays(1)))
                .sleepStart(LocalTime.of(23,0,0))
                .sleepEnd(LocalTime.of(8,0,0))
                .timeInBed(Duration.of(9, ChronoUnit.HOURS))
                .wakeUpFeeling(WakeUpFeeling.GOOD)
                .build();

        assertEquals(expectedSleepSessionDTO, SleepSessionMapper.toDTO(sleepSession));
    }

    @Test
    public void shouldMapSleepSessionDtoToEntity(){
        SleepSession expectedSleepSession = SleepSession
                .builder()
                .sleepDate(java.sql.Date.valueOf(LocalDate.now().minusDays(1)))
                .sleepStart(Time.valueOf(LocalTime.of(23, 0)))
                .sleepEnd(Time.valueOf(LocalTime.of(8, 0)))
                .wakeUpFeeling(WakeUpFeeling.GOOD)
                .build();

        SleepSessionDTO sleepSessionDTO = SleepSessionDTO
                .builder()
                .sleepDate(java.sql.Date.valueOf(LocalDate.now().minusDays(1)))
                .sleepStart(LocalTime.of(23,0,0))
                .sleepEnd(LocalTime.of(8,0,0))
                .timeInBed(Duration.of(9, ChronoUnit.HOURS))
                .wakeUpFeeling(WakeUpFeeling.GOOD)
                .build();

        assertEquals(expectedSleepSession, SleepSessionMapper.toEntity(sleepSessionDTO));
    }
}
