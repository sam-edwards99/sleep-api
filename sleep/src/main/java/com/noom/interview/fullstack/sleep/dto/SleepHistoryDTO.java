package com.noom.interview.fullstack.sleep.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.noom.interview.fullstack.sleep.util.CustomDurationSerializer;
import com.noom.interview.fullstack.sleep.util.WakeUpFeeling;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;


@Data
@Builder
public class SleepHistoryDTO {
    private Date dateRangeStart;
    private Date dateRangeEnd;
    private Long count;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime avgSleepStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime avgSleepEnd;

    @JsonSerialize(using = CustomDurationSerializer.class)
    private Duration avgTimeInBed;

    private Map<WakeUpFeeling, Integer> wakeUpFeelings;

}
