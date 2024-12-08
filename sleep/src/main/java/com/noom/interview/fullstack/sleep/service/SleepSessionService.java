package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.SleepHistoryDTO;
import com.noom.interview.fullstack.sleep.dto.SleepSessionDTO;
import com.noom.interview.fullstack.sleep.entity.SleepSession;
import com.noom.interview.fullstack.sleep.exception.SleepSessionNotFoundException;
import com.noom.interview.fullstack.sleep.exception.SleepSessionNotFoundInLastThirtyDaysException;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
import com.noom.interview.fullstack.sleep.mapper.SleepSessionMapper;
import com.noom.interview.fullstack.sleep.repository.SleepSessionRepository;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import com.noom.interview.fullstack.sleep.util.WakeUpFeeling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SleepSessionService {

    private final SleepSessionRepository sleepSessionRepository;
    private final UserRepository userRepository;

    public SleepSessionService(@Autowired SleepSessionRepository sleepSessionRepository, @Autowired UserRepository userRepository) {
        this.sleepSessionRepository = sleepSessionRepository;
        this.userRepository = userRepository;
    }

    // Fetch information about the last night's sleep
    public SleepSessionDTO getSleepSession(Long userId) {
        SleepSession sleepSession = sleepSessionRepository.findTopBySleeperIdOrderBySleepDateDesc(userId)
                .orElseThrow(SleepSessionNotFoundException::new);
        return SleepSessionMapper.toDTO(sleepSession);
    }

    // Create the sleep log for the last night
    public SleepSessionDTO createNewSleepSession(Long userId, SleepSessionDTO sleepSessionDTO) {
        // Conditionally throw exception to trigger 404 if userId does not exist in DB
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        sleepSessionDTO.setSleeperId(userId);
        SleepSession sleepSession = sleepSessionRepository.save(SleepSessionMapper.toEntity(sleepSessionDTO));
        return SleepSessionMapper.toDTO(sleepSession);
    }

    // Get the 30-day sleep history data
    public SleepHistoryDTO getThirtyDaySleepHistory(Long userId) {
        // get all sleep sessions from the past 30 days
        Date thirtyDaysAgo = Date.valueOf(LocalDate.now().minusDays(30));
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<SleepSession> sleepSessions = sleepSessionRepository.getSleepSessionsBySleeperIdAndSleepDateAfter(userId, thirtyDaysAgo);

        // throw exception to trigger 404 if no sleep sessions are returned
        if (sleepSessions.isEmpty()) {
            throw new SleepSessionNotFoundInLastThirtyDaysException();
        } else {
            // count and sums for calculating averages
            Long count = (long) sleepSessions.size();
            Long totalSleepEnd = 0L, totalTimeInBed = 0L;

            // The range for which averages are shown
            Date firstDayOfInterval = sleepSessions.get(0).getSleepDate();
            Date lastDayOfInterval = sleepSessions.get(0).getSleepDate();

            // Frequencies of how the user felt in the morning
            Map<WakeUpFeeling, Integer> wakeUpFeelingMap = new HashMap<>();

            for (SleepSession sleepSession : sleepSessions) {
                // get min and max date range
                if (sleepSession.getSleepDate().before(firstDayOfInterval)) {
                    firstDayOfInterval = sleepSession.getSleepDate();
                }
                if (sleepSession.getSleepDate().after(lastDayOfInterval)) {
                    lastDayOfInterval = sleepSession.getSleepDate();
                }

                // account for start time being after end time due to midnight
                //      ex: User goes to sleep at 11pm (23:00) and wakes up at 8am (08:00)
                if(sleepSession.getSleepStart().after(sleepSession.getSleepEnd())) {
                    Timestamp startTimestamp = new Timestamp(sleepSession.getSleepStart().getTime());
                    Timestamp endTimestamp = new Timestamp(sleepSession.getSleepEnd().getTime());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTimestamp.getTime());

                    // Add 24 hours to end time so that time in bed is calculated accurately
                    //      ex: User goes to sleep at 11pm (23:00) and wakes up at 8am (08:00)
                    //          Now wake-up time is shifted to 32:00 so time in bed = 9hrs
                    calendar.add(Calendar.DAY_OF_MONTH, 1);

                    endTimestamp = new Timestamp(calendar.getTimeInMillis());
                    totalTimeInBed += Duration.between(startTimestamp.toLocalDateTime(), endTimestamp.toLocalDateTime()).toMillis();
                }

                // account for start time being before end time due to midnight
                //      ex: User goes to sleep at 1am (01:00) and wakes up at 8am (08:00)
                else {
                    totalTimeInBed += Duration.between(sleepSession.getSleepStart().toLocalTime(), sleepSession.getSleepEnd().toLocalTime()).toMillis();
                }

                totalSleepEnd += sleepSession.getSleepEnd().getTime();

                // Load wake up feeling counts into a frequency map
                wakeUpFeelingMap.put(sleepSession.getWakeUpFeeling(), wakeUpFeelingMap.getOrDefault(sleepSession.getWakeUpFeeling(), 0) + 1);
            }

            // Average total time in bed
            Duration avgSleepDuration = Duration.ofMillis(totalTimeInBed / count);

            // The average time the user gets to bed and gets out of bed
            LocalTime avgSleepEnd = new Time(totalSleepEnd/count).toLocalTime();

            // Synthesize this data from avg duration and avg end time to account for midnight skewing the average
            LocalTime avgSleepStart = avgSleepEnd.minus(avgSleepDuration);

            return SleepHistoryDTO.builder()
                    .dateRangeStart(firstDayOfInterval)
                    .dateRangeEnd(lastDayOfInterval)
                    .count(count)
                    .avgTimeInBed(avgSleepDuration)
                    .avgSleepStart(avgSleepStart)
                    .avgSleepEnd(avgSleepEnd)
                    .wakeUpFeelings(wakeUpFeelingMap)
                    .build();
        }
    }
}