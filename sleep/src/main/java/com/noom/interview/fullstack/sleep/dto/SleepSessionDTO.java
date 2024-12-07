package com.noom.interview.fullstack.sleep.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.noom.interview.fullstack.sleep.util.CustomDurationSerializer;
import com.noom.interview.fullstack.sleep.util.WakeUpFeeling;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class SleepSessionDTO {
    private Long id;

    private Long sleeperId;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date sleepDate;

    // The time in bed interval (Can use these to derive Total time in bed)
    @JsonFormat(pattern="HH:mm")
    private LocalTime sleepStart;

    @JsonFormat(pattern="HH:mm")
    private LocalTime sleepEnd;

    @JsonSerialize(using = CustomDurationSerializer.class)
    private Duration timeInBed;

    // How the user felt in the morning: one of [BAD, OK, GOOD]
    private WakeUpFeeling wakeUpFeeling;
}
