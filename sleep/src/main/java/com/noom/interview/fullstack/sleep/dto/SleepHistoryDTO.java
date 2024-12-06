package com.noom.interview.fullstack.sleep.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.noom.interview.fullstack.sleep.util.WakeUpFeeling;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;


@Data
@Builder
public class SleepHistoryDTO {
    private Date dateRangeStart;
    private Date dateRangeEnd;
    private Long count;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Timestamp avgSleepStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Timestamp avgSleepEnd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Time avgTimeInBed;

    private Map<WakeUpFeeling, Integer> wakeUpFeelings;

}
