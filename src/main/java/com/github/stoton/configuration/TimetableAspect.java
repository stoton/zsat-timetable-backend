package com.github.stoton.configuration;

import com.github.stoton.domain.Statistics;
import com.github.stoton.repository.StatisticsRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Aspect
@Component
public class TimetableAspect {

    private Logger logger = Logger.getLogger(getClass().getName());

    private StatisticsRepository statisticsRepository;

    @Autowired
    public TimetableAspect(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @After("execution(* com.github.stoton.controller.TimetableItemController.getTimetableIndexItems())")
    public void logBefore() {
        logger.info("Request for timetable index items.");

        Statistics statistics = new Statistics();
        statistics.setDateTimeOfView(LocalDateTime.now());
        statisticsRepository.save(statistics);
    }

}
