package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.SleepSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface SleepSessionRepository extends JpaRepository<SleepSession,Integer> {
    List<SleepSession> getSleepSessionsBySleeperIdAndSleepDateAfter(Long sleeperId, Date sleepDateAfter);

    Optional<SleepSession> findTopBySleeperIdOrderBySleepDateDesc(Long userId);

}