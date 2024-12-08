package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.dto.SleepSessionDTO;
import com.noom.interview.fullstack.sleep.entity.SleepSession;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Calendar;

// Helper class for converting entities to DTOs and vice versa
//      It wasn't strictly necessary to have both DTOs and Entities for this class for this small application
//      but felt it was better practice to keep them separate as this allows the different layers of the application to be
//      more loosely coupled

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

    // generates the timeInBed duration using the start and end fields for the DTO object
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
