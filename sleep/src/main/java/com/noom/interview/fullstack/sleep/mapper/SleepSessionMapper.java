package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.dto.SleepSessionDTO;
import com.noom.interview.fullstack.sleep.dto.UserDTO;
import com.noom.interview.fullstack.sleep.entity.SleepSession;
import com.noom.interview.fullstack.sleep.entity.User;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;

public class SleepSessionMapper {
    public static SleepSessionDTO toDTO(SleepSession entity) {
        return SleepSessionDTO.builder()
                .id(entity.getId())
                .sleeperId(entity.getSleeperId())
                .sleepDate(entity.getSleepDate())
                .sleepStart(entity.getSleepStart().toLocalTime())
                .sleepEnd(entity.getSleepEnd().toLocalTime())
                .timeInBed(getTimeInBedFromSleepInterval(entity.getSleepStart(), entity.getSleepEnd()))
                .wakeUpFeeling(entity.getWakeUpFeeling())
                .build();
    }

    public static SleepSession toEntity(SleepSessionDTO dto) {
        return SleepSession.builder()
                .sleeperId(dto.getSleeperId())
                .sleepDate(new Date(dto.getSleepDate().getTime()))
                .sleepStart(Time.valueOf(dto.getSleepStart()))
                .sleepEnd(Time.valueOf(dto.getSleepEnd()))
                .wakeUpFeeling(dto.getWakeUpFeeling())
                .build();
    }

    private static Duration getTimeInBedFromSleepInterval(Time sleepStart, Time sleepEnd) {
        Timestamp startTimestamp = new Timestamp(sleepStart.getTime());
        Timestamp endTimestamp = new Timestamp(sleepEnd.getTime());

        // account for midnight throwing off the duration calculation
        if(startTimestamp.after(endTimestamp)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(endTimestamp.getTime());

            // Add one day to end time so that time in bed is calculated accurately
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            endTimestamp = new Timestamp(calendar.getTimeInMillis());
        }
        return Duration.between(startTimestamp.toLocalDateTime(), endTimestamp.toLocalDateTime());

    }

}
