package com.github.stoton;

import com.github.stoton.repository.CacheJsonRepository;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.service.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App   {

    @Autowired
    private Parser parser;

    @Autowired
    private TimetableIndexItemRepository timetableIndexItemRepository;

    @Autowired
    private CacheJsonRepository cacheJsonRepository;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
