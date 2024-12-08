package com.noom.interview.fullstack.sleep.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.noom.interview.fullstack.sleep.util.CustomDurationSerializer;
import com.noom.interview.fullstack.sleep.util.WakeUpFeeling;
import com.noom.interview.fullstack.sleep.validator.SleepDateConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

// DTO for a single sleep session
@Data
@AllArgsConstructor
@Builder
public class SleepSessionDTO {
    private Long id;

    private Long sleeperId;


    @NotNull(message = "Must include sleepDate")
    @SleepDateConstraint
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date sleepDate;

    // The time in bed interval (Can use these to derive Total time in bed)
    @NotNull(message = "Must include sleepStart")
    @JsonFormat(pattern="HH:mm")
    private LocalTime sleepStart;

    @NotNull(message = "Must include sleepEnd")
    @JsonFormat(pattern="HH:mm")
    private LocalTime sleepEnd;

    @JsonSerialize(using = CustomDurationSerializer.class)
    private Duration timeInBed;

    // How the user felt in the morning: one of [BAD, OK, GOOD]
    @NotNull(message = "Must include wakeUpFeeling")
    private WakeUpFeeling wakeUpFeeling;
}
