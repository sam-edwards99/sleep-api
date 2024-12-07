package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.SleepHistoryDTO;
import com.noom.interview.fullstack.sleep.dto.SleepSessionDTO;
import com.noom.interview.fullstack.sleep.entity.SleepSession;
import com.noom.interview.fullstack.sleep.exception.SleepSessionNotFoundException;
import com.noom.interview.fullstack.sleep.exception.SleepSessionNotFoundInLastThirtyDaysException;
import com.noom.interview.fullstack.sleep.mapper.SleepSessionMapper;
import com.noom.interview.fullstack.sleep.repository.SleepSessionRepository;
import com.noom.interview.fullstack.sleep.util.WakeUpFeeling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SleepSessionService {

    @Autowired
    private SleepSessionRepository sleepSessionRepository;

    // Fetch information about the last night's sleep
    public SleepSession getSleepSession(Long userId) {
        return sleepSessionRepository.findTopBySleeperIdOrderBySleepDateDesc(userId).orElseThrow(SleepSessionNotFoundException::new);
    }

    // Create the sleep log for the last night
    public SleepSession createNewSleepSession(Long userId, SleepSessionDTO sleepSession) {
        sleepSession.setSleeperId(userId);
        return sleepSessionRepository.save(SleepSessionMapper.toEntity(sleepSession));
    }

    // Get the 30 day sleep history data
    public SleepHistoryDTO getThirtyDaySleepHistory(Long userId) {
        Date thirtyDaysAgo = Date.valueOf(LocalDate.now().minusDays(30));
        List<SleepSession> sleepSessions = sleepSessionRepository.getSleepSessionsBySleeperIdAndSleepDateAfter(userId, thirtyDaysAgo);
        // throw exception if no sleep sessions are returned
        if (sleepSessions.isEmpty()) {
            throw new SleepSessionNotFoundInLastThirtyDaysException();
        } else {
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
                if(sleepSession.getSleepStart().after(sleepSession.getSleepEnd())) {
                    Timestamp startTimestamp = new Timestamp(sleepSession.getSleepStart().getTime());
                    Timestamp endTimestamp = new Timestamp(sleepSession.getSleepEnd().getTime());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endTimestamp.getTime());

                    // Add one day to end time so that time in bed is calculated accurately
                    calendar.add(Calendar.DAY_OF_MONTH, 1);

                    endTimestamp = new Timestamp(calendar.getTimeInMillis());
                    totalTimeInBed += Duration.between(startTimestamp.toLocalDateTime(), endTimestamp.toLocalDateTime()).toMillis();
                }
                else {
                    Timestamp startTimestamp = new Timestamp(sleepSession.getSleepStart().getTime());
                    totalTimeInBed += Duration.between(sleepSession.getSleepStart().toLocalTime(), sleepSession.getSleepEnd().toLocalTime()).toMillis();
                }

                totalSleepEnd += sleepSession.getSleepEnd().getTime();
                wakeUpFeelingMap.put(sleepSession.getWakeUpFeeling(), wakeUpFeelingMap.getOrDefault(sleepSession.getWakeUpFeeling(), 0) + 1);
            }

            // Average total time in bed
            Time avgSleepDuration = new Time(totalTimeInBed / count);

            // The average time the user gets to bed and gets out of bed
            Timestamp avgSleepEnd = new Timestamp(totalSleepEnd / count);

            // Synthesize this data from avg duration and avg end time to account for midnight skewing the average
            Timestamp avgSleepStart = new Timestamp(avgSleepEnd.getTime() - avgSleepDuration.getTime());

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