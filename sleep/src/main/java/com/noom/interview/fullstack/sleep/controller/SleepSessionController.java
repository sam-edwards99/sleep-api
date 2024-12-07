package com.noom.interview.fullstack.sleep.controller;

import com.noom.interview.fullstack.sleep.dto.SleepHistoryDTO;
import com.noom.interview.fullstack.sleep.dto.SleepSessionDTO;
import com.noom.interview.fullstack.sleep.entity.SleepSession;
import com.noom.interview.fullstack.sleep.service.SleepSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/{id}")
public class SleepSessionController {
    @Autowired
    SleepSessionService sleepSessionService;

    // get the previous sleep entry
    @GetMapping("/sleep")
    public SleepSession getLastNightSleepDataByUserId(@PathVariable("id") Long userId) {
        return sleepSessionService.getSleepSession(userId);
    }

    // create a new sleep entry
    @PostMapping("/sleep")
    public SleepSession createNewSleepSession(@PathVariable("id") Long userId, @RequestBody SleepSessionDTO sleepSession) {
        return sleepSessionService.createNewSleepSession(userId, sleepSession);
    }

    // get the 30 day sleep history data
    @GetMapping("/sleep-history")
    public SleepHistoryDTO getSleepHistory(@PathVariable("id") Long userId) {
        return sleepSessionService.getThirtyDaySleepHistory(userId);
    }
}
