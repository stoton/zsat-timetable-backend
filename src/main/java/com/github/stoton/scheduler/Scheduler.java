package com.github.stoton.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stoton.domain.Cache;
import com.github.stoton.domain.CacheJson;
import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.domain.TimetableIndexItem;
import com.github.stoton.repository.CacheJsonRepository;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Scheduler {

    private static final String ROOT_URL = "http://szkola.zsat.linuxpl.eu/planlekcji/";

    private static final int TWENTY_FOUR_HOURS = 3600000 * 24;

    private static final int ONE_SECOND = 1;

    private Parser parser;

    private TimetableIndexItemRepository timetableIndexItemRepository;

    private CacheJsonRepository cacheJsonRepository;

    @Autowired
    public Scheduler(Parser parser, TimetableIndexItemRepository timetableIndexItemRepository, CacheJsonRepository cacheJsonRepository) {
        this.parser = parser;
        this.timetableIndexItemRepository = timetableIndexItemRepository;
        this.cacheJsonRepository = cacheJsonRepository;
    }

    @Scheduled(fixedDelay = TWENTY_FOUR_HOURS, initialDelay = ONE_SECOND)
    public void collectData() {

        ObjectMapper mapper = new ObjectMapper();

        List<TimetableIndexItem> timetableIndexItems = timetableIndexItemRepository.findAll();

        List<Cache> caches = new ArrayList<>();

        for(TimetableIndexItem timetableIndexItem : timetableIndexItems) {
            final String url = ROOT_URL + timetableIndexItem.getUrl();
            final String type = timetableIndexItem.getType();

            CompleteTimetable completeTimetable = null;
            try {
                completeTimetable = parser.parseZsatDocument(url, type);
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }

            Cache cache = Cache.builder()
                    .name(timetableIndexItem.getName())
                    .timetable(completeTimetable)
                    .build();

            caches.add(cache);
        }

        CacheJson cacheJson = new CacheJson();

        try {
            cacheJson.setJson(mapper.writeValueAsString(caches));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        cacheJsonRepository.deleteAll();
        cacheJsonRepository.save(cacheJson);
    }
}
