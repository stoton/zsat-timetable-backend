package com.github.stoton.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stoton.domain.Cache;
import com.github.stoton.domain.CacheJson;
import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.domain.TimetableIndexItem;
import com.github.stoton.repository.CacheJsonRepository;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.service.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Scheduler {

    private static final String ROOT_URL = "http://szkola.zsat.linuxpl.eu/planlekcji/";

    private Parser parser;

    private TimetableIndexItemRepository timetableIndexItemRepository;

    private CacheJsonRepository cacheJsonRepository;

    @Scheduled(fixedDelay = 3600000 * 24, initialDelay = 1)
    public void collectData() {

        ObjectMapper mapper = new ObjectMapper();

        List<TimetableIndexItem> timetableIndexItems = timetableIndexItemRepository.findAll();

        List<Cache> caches = new ArrayList<>();

        timetableIndexItems
                .forEach(timetableIndexItem -> {
                    final String url = ROOT_URL + timetableIndexItem.getUrl();
                    final String type = timetableIndexItem.getType();

                    final CompleteTimetable completeTimetable = parser.parseDataFromZsat(url, type);

                    Cache cache = Cache.builder()
                            .name(timetableIndexItem.getName())
                            .timetable(completeTimetable)
                            .build();

                    caches.add(cache);
                });

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
