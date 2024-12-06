package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.SleepHistoryDTO;
import com.noom.interview.fullstack.sleep.entity.SleepSession;
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
    public SleepSession getSleepLogEntry(Long userId) {
        return sleepSessionRepository.getTopBySleeperIdOrderBySleepDateDesc(userId);
    }

    // Create the sleep log for the last night
    public SleepSession createNewSleepLogEntry(Long userId, SleepSession sleepLogEntry) {
        sleepLogEntry.setSleeperId(userId);
        return sleepSessionRepository.save(sleepLogEntry);
    }

    // Get the 30 day sleep history data
    public SleepHistoryDTO getThirtyDaySleepHistory(Long userId) {
        Date thirtyDaysAgo = Date.valueOf(LocalDate.now().minusDays(30));
        List<SleepSession> sleepSessions = sleepSessionRepository.getSleepSessionsBySleeperIdAndSleepDateAfter(userId, thirtyDaysAgo);
        System.out.println(sleepSessions.size());
        if (sleepSessions.isEmpty()) {
            return null;
        } else {
            Long totalTimeInBed = 0L;
            Long count = (long) sleepSessions.size();
            Long totalSleepEnd = 0L, totalSleepStart = 0L;

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
                    Timestamp startDate = new Timestamp(sleepSession.getSleepStart().getTime());
                    Timestamp endDate = new Timestamp(sleepSession.getSleepEnd().getTime());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(endDate.getTime());

                    // Add one day to the Calendar
                    calendar.add(Calendar.DAY_OF_MONTH, 1);

                    // Create a new Timestamp from the updated Calendar
                    endDate = new Timestamp(calendar.getTimeInMillis());

                    totalTimeInBed += Duration.between(startDate.toLocalDateTime(), endDate.toLocalDateTime()).toMillis();
                }
                else {
                    totalTimeInBed += Duration.between(sleepSession.getSleepStart().toLocalTime(), sleepSession.getSleepEnd().toLocalTime()).toMillis();
                }

                totalSleepStart += sleepSession.getSleepStart().getTime();
                totalSleepEnd += sleepSession.getSleepEnd().getTime();
                wakeUpFeelingMap.put(sleepSession.getWakeUpFeeling(), wakeUpFeelingMap.getOrDefault(sleepSession.getWakeUpFeeling(), 0) + 1);
            }
            // The average time the user gets to bed and gets out of bed
            Timestamp avgSleepStart = new Timestamp(totalSleepStart / count);
            Timestamp avgSleepEnd = new Timestamp(totalSleepEnd / count);

            // Average total time in bed
            Time avgSleepDuration = new Time(totalTimeInBed / count);

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