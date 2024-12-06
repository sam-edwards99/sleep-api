package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.SleepSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;


@Repository
public interface SleepSessionRepository extends JpaRepository<SleepSession,Integer> {
    List<SleepSession> getSleepSessionsBySleeperIdAndSleepDateAfter(Long sleeperId, Date sleepDateAfter);

    SleepSession getTopBySleeperIdOrderBySleepDateDesc(Long sleeperId);
}