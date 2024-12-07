package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.dto.SleepSessionDTO;
import com.noom.interview.fullstack.sleep.dto.UserDTO;
import com.noom.interview.fullstack.sleep.entity.SleepSession;
import com.noom.interview.fullstack.sleep.entity.User;

import java.sql.Date;

public class SleepSessionMapper {
    public static SleepSessionDTO toDTO(SleepSession entity) {
        return SleepSessionDTO.builder()
                .id(entity.getId())
                .sleeperId(entity.getSleeperId())
                .sleepDate(entity.getSleepDate())
                .sleepStart(entity.getSleepStart())
                .sleepEnd(entity.getSleepEnd())
                .wakeUpFeeling(entity.getWakeUpFeeling())
                .build();
    }

    public static SleepSession toEntity(SleepSessionDTO dto) {
        return SleepSession.builder()
                .sleeperId(dto.getSleeperId())
                .sleepDate(new Date(dto.getSleepDate().getTime()))
                .sleepStart(dto.getSleepStart())
                .sleepEnd(dto.getSleepEnd())
                .wakeUpFeeling(dto.getWakeUpFeeling())
                .build();
    }

}
