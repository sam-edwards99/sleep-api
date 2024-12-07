package com.noom.interview.fullstack.sleep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noom.interview.fullstack.sleep.dto.SleepHistoryDTO;
import com.noom.interview.fullstack.sleep.dto.SleepSessionDTO;
import com.noom.interview.fullstack.sleep.exception.SleepSessionNotFoundException;
import com.noom.interview.fullstack.sleep.exception.SleepSessionNotFoundInLastThirtyDaysException;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
import com.noom.interview.fullstack.sleep.service.SleepSessionService;
import com.noom.interview.fullstack.sleep.util.WakeUpFeeling;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @WebMvcTest(SleepSessionController.class)
    public class SleepSessionControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private SleepSessionService service;

        @Test
        public void getLastSleepSessionByUserIdShouldReturn200() throws Exception {
            SleepSessionDTO sleepSessionDTO = SleepSessionDTO
                    .builder()
                    .sleepDate(new Date())
                    .sleepStart(LocalTime.of(23,0,0))
                    .sleepEnd(LocalTime.of(8,0,0))
                    .timeInBed(Duration.of(9, ChronoUnit.HOURS))
                    .wakeUpFeeling(WakeUpFeeling.GOOD)
                    .build();

            when(service.getSleepSession(anyLong())).thenReturn(sleepSessionDTO);

            MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                            .get("/user/1/sleep"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();
            assertEquals(objectMapper.writeValueAsString(sleepSessionDTO), result.getResponse().getContentAsString());
        }

        @Test
        public void getLastSleepSessionByUserIdShouldReturn404IfNoSleepSessionsFoundForUserId() throws Exception {
            when(service.getSleepSession(anyLong())).thenThrow(SleepSessionNotFoundException.class);

            this.mockMvc.perform(MockMvcRequestBuilders
                            .get("/user/1/sleep"))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        public void createNewSleepSessionShouldReturn200() throws Exception {
            SleepSessionDTO sleepSessionDTO = SleepSessionDTO
                    .builder()
                    .sleepDate(new Date())
                    .sleepStart(LocalTime.of(23,0,0))
                    .sleepEnd(LocalTime.of(8,0,0))
                    .wakeUpFeeling(WakeUpFeeling.GOOD)
                    .build();

            String requestBody = objectMapper.writeValueAsString(sleepSessionDTO);

            when(service.createNewSleepSession(anyLong(), any(SleepSessionDTO.class))).thenReturn(sleepSessionDTO);

            MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                            .post("/user/1/sleep")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();
            assertEquals(objectMapper.writeValueAsString(sleepSessionDTO), result.getResponse().getContentAsString());
        }

        @Test
        public void createNewSleepSessionShouldReturn404IfUserDoesNotExist() throws Exception {
            SleepSessionDTO sleepSessionDTO = SleepSessionDTO
                    .builder()
                    .sleepDate(new Date())
                    .sleepStart(LocalTime.of(23,0,0))
                    .sleepEnd(LocalTime.of(8,0,0))
                    .wakeUpFeeling(WakeUpFeeling.GOOD)
                    .build();

            String requestBody = objectMapper.writeValueAsString(sleepSessionDTO);

            when(service.createNewSleepSession(anyLong(), any(SleepSessionDTO.class))).thenThrow(UserNotFoundException.class);

            this.mockMvc.perform(MockMvcRequestBuilders
                            .post("/user/1/sleep")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        public void getSleepHistoryShouldReturn200() throws Exception {
            SleepHistoryDTO sleepHistoryDTO = SleepHistoryDTO
                    .builder()
                    .dateRangeStart(new Date())
                    .dateRangeEnd(new Date())
                    .avgSleepStart(LocalTime.of(23,0,0))
                    .avgSleepEnd(LocalTime.of(8,0,0))
                    .avgTimeInBed(Duration.of(9, ChronoUnit.HOURS))
                    .wakeUpFeelings(Map.of(WakeUpFeeling.GOOD, 1))
                    .build();

            when(service.getThirtyDaySleepHistory(anyLong())).thenReturn(sleepHistoryDTO);

            MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                            .get("/user/1/sleep-history"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();
            assertEquals(objectMapper.writeValueAsString(sleepHistoryDTO), result.getResponse().getContentAsString());
        }

        @Test
        public void getSleepHistoryShouldReturn404IfNoSleepSessionsFoundForUserInLastThirtyDays() throws Exception {
            when(service.getThirtyDaySleepHistory(anyLong())).thenThrow(SleepSessionNotFoundInLastThirtyDaysException.class);

            this.mockMvc.perform(MockMvcRequestBuilders
                            .get("/user/1/sleep-history"))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

}
