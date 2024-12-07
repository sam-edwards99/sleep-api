package com.noom.interview.fullstack.sleep.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.noom.interview.fullstack.sleep.util.WakeUpFeeling;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "sleep_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sleeperId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd")
    private Date sleepDate;

    // The time in bed interval (Can use these to derive Total time in bed)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Time sleepStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Time sleepEnd;

    // How the user felt in the morning: one of [BAD, OK, GOOD]
    @Enumerated(EnumType.STRING)
    private WakeUpFeeling wakeUpFeeling;
}