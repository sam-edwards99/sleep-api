package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.SleepHistoryDTO;
import com.noom.interview.fullstack.sleep.dto.SleepSessionDTO;
import com.noom.interview.fullstack.sleep.entity.SleepSession;
import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.exception.SleepSessionNotFoundException;
import com.noom.interview.fullstack.sleep.exception.SleepSessionNotFoundInLastThirtyDaysException;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
import com.noom.interview.fullstack.sleep.mapper.SleepSessionMapper;
import com.noom.interview.fullstack.sleep.repository.SleepSessionRepository;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.util.WakeUpFeeling;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.noom.interview.fullstack.sleep.SleepApplication.UNIT_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles(UNIT_TEST_PROFILE)
public class SleepSessionServiceTest {
    @InjectMocks
    private SleepSessionService sleepSessionService;

    @Mock
    private SleepSessionRepository sleepSessionRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void getSleepSessionShouldReturnMostRecentSession() {
         SleepSessionDTO sleepSessionDTO = SleepSessionDTO
                 .builder()
                 .sleepDate(new Date())
                 .sleepStart(LocalTime.of(23,0,0))
                 .sleepEnd(LocalTime.of(8,0,0))
                 .timeInBed(Duration.of(9, ChronoUnit.HOURS))
                 .wakeUpFeeling(WakeUpFeeling.GOOD)
                 .build();

         SleepSession sleepSession = SleepSessionMapper.toEntity(sleepSessionDTO);

         when(sleepSessionRepository.findTopBySleeperIdOrderBySleepDateDesc(1L)).thenReturn(Optional.of(sleepSession));

         assertEquals(sleepSessionDTO, sleepSessionService.getSleepSession(1L));
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserNotFound() {
        when(sleepSessionRepository.findTopBySleeperIdOrderBySleepDateDesc(1L)).thenReturn(Optional.empty());

        assertThrows(SleepSessionNotFoundException.class, () -> sleepSessionService.getSleepSession(1L));
    }

    @Test
    void createNewSleepSessionShouldReturnNewlyCreatedSession() {
        SleepSessionDTO sleepSessionDTO = SleepSessionDTO
                .builder()
                .sleeperId(1L)
                .sleepDate(new Date())
                .sleepStart(LocalTime.of(23,0,0))
                .sleepEnd(LocalTime.of(8,0,0))
                .timeInBed(Duration.of(9, ChronoUnit.HOURS))
                .wakeUpFeeling(WakeUpFeeling.GOOD)
                .build();

        SleepSession sleepSession = SleepSessionMapper.toEntity(sleepSessionDTO);

        User user = User.builder().id(1L).name("bob").build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(sleepSessionRepository.save(any(SleepSession.class))).thenReturn(sleepSession);

        assertEquals(sleepSessionDTO, sleepSessionService.createNewSleepSession(1L, sleepSessionDTO));
    }

    @Test
    void createNewSleepSessionShouldThrowExceptionWhenUserNotFound() {
        SleepSessionDTO sleepSessionDTO = SleepSessionDTO
                .builder()
                .sleepDate(new Date())
                .sleepStart(LocalTime.of(23,0,0))
                .sleepEnd(LocalTime.of(8,0,0))
                .timeInBed(Duration.of(9, ChronoUnit.HOURS))
                .wakeUpFeeling(WakeUpFeeling.GOOD)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> sleepSessionService.createNewSleepSession(1L, sleepSessionDTO));
    }

    @Test
    void getThirtyDaySleepHistoryShouldCorrectlyCalculateSleepHistory() {
        SleepSessionDTO sleepSessionDTO = SleepSessionDTO
                .builder()
                .sleepDate(new Date())
                .sleepStart(LocalTime.of(23,0,0))
                .sleepEnd(LocalTime.of(8,0,0))
                .timeInBed(Duration.of(9, ChronoUnit.HOURS))
                .wakeUpFeeling(WakeUpFeeling.GOOD)
                .build();



        SleepSession sleepSession1 = SleepSession.builder()
                .id(1L)
                .sleeperId(1L)
                .sleepDate(java.sql.Date.valueOf(LocalDate.now().minusDays(1)))
                .sleepStart(Time.valueOf(LocalTime.of(23, 0)))
                .sleepEnd(Time.valueOf(LocalTime.of(8, 0)))
                .wakeUpFeeling(WakeUpFeeling.OK)
                .build();

        SleepSession sleepSession2 = SleepSession.builder()
                .id(1L)
                .sleeperId(1L)
                .sleepDate(java.sql.Date.valueOf(LocalDate.now().minusDays(3)))
                .sleepStart(Time.valueOf(LocalTime.of(1, 0)))
                .sleepEnd(Time.valueOf(LocalTime.of(9, 0)))
                .wakeUpFeeling(WakeUpFeeling.BAD)
                .build();

        User user = User.builder().id(1L).name("bob").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(sleepSessionRepository.getSleepSessionsBySleeperIdAndSleepDateAfter(anyLong(), any(java.sql.Date.class)))
                .thenReturn(List.of(sleepSession1, sleepSession2));

        SleepHistoryDTO expected = SleepHistoryDTO
                .builder()
                .dateRangeStart(java.sql.Date.valueOf(LocalDate.now().minusDays(3)))
                .dateRangeEnd(java.sql.Date.valueOf(LocalDate.now().minusDays(1)))
                .count(2L)
                .avgSleepStart(LocalTime.of(0, 0 ,0))
                .avgSleepEnd(LocalTime.of(8,30,0))
                .avgTimeInBed(Duration.of(510, ChronoUnit.MINUTES))
                .wakeUpFeelings(Map.of(WakeUpFeeling.OK, 1, WakeUpFeeling.BAD, 1))
                .build();
        assertEquals(expected, sleepSessionService.getThirtyDaySleepHistory(1L));
    }

    @Test
    void getThirtyDaySleepHistoryShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> sleepSessionService.getThirtyDaySleepHistory(1L));
    }

    @Test
    void getThirtyDaySleepHistoryShouldThrowExceptionWhenThereAreNoSleepSessionsForUser() {
        User user = User.builder().id(1L).name("bob").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sleepSessionRepository.getSleepSessionsBySleeperIdAndSleepDateAfter(anyLong(), any(java.sql.Date.class)))
                .thenReturn(Collections.emptyList());

        assertThrows(SleepSessionNotFoundInLastThirtyDaysException.class, () -> sleepSessionService.getThirtyDaySleepHistory(1L));
    }

}
