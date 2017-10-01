package com.github.stoton;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stoton.domain.Cache;
import com.github.stoton.domain.CacheJson;
import com.github.stoton.domain.DayContainer;
import com.github.stoton.domain.TimetableIndexItem;
import com.github.stoton.repository.CacheJsonRepository;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.service.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class App {

    @Autowired
    private Parser parser;

    @Autowired
    private TimetableIndexItemRepository timetableIndexItemRepository;

    @Autowired
    private CacheJsonRepository cacheJsonRepository;

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(App.class);
//    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Scheduled(fixedDelay = 3600000 * 24, initialDelay = 1)
    public void collectData() {
        ObjectMapper mapper = new ObjectMapper();
        List<TimetableIndexItem> timetableIndexItems = timetableIndexItemRepository.findAll();
        List<Cache> list = new ArrayList<>();

        for(TimetableIndexItem indexItem : timetableIndexItems) {
            Cache cache = new Cache();
            String url = "http://szkola.zsat.linuxpl.eu/planlekcji/" + indexItem.getUrl();
            DayContainer dayContainer = parser.parseDataFromZsat(url, indexItem.getType());
            cache.setName(indexItem.getName());
            cache.setTimetable(dayContainer);
            list.add(cache);
        }

        CacheJson cacheJson = new CacheJson();

        try {
            cacheJson.setJson(mapper.writeValueAsString(list));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        cacheJsonRepository.deleteAll();
        cacheJsonRepository.save(cacheJson);
    }
}
