package com.github.stoton.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.github.stoton.domain.Cache;
import com.github.stoton.domain.CacheJson;
import com.github.stoton.domain.DayContener;
import com.github.stoton.domain.TimetableIndexItem;
import com.github.stoton.repository.CacheJsonRepository;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.service.Parser;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TimetableItemController {

    @Autowired
    private Parser parser;

    @Autowired
    private TimetableIndexItemRepository timetableIndexItemRepository;

    @Autowired
    private CacheJsonRepository cacheJsonRepository;

    @GetMapping("/timetable/{name:.+}")
    HttpEntity<DayContener> parseTimetable(@PathVariable String name) throws IOException {

        TimetableIndexItem timetableIndexItem = timetableIndexItemRepository.findFirstByName(name);

        if(timetableIndexItem == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String url = "http://szkola.zsat.linuxpl.eu/planlekcji/" + timetableIndexItem.getUrl();
        DayContener dayContener = parser.parseDataFromZsat(url, timetableIndexItem.getType());

        if(dayContener != null) {
            return ResponseEntity.ok(dayContener);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/timetable")
    HttpEntity<List<TimetableIndexItem> > indexItems() throws IOException {
        List<TimetableIndexItem> list =  parser.parseDataFromZsatTimetableIndex();

        timetableIndexItemRepository.deleteAll();

        for(TimetableIndexItem t : list) {
            TimetableIndexItem current = timetableIndexItemRepository.findFirstByName(t.getName());
            if(current == null) {
                timetableIndexItemRepository.save(t);
            }
        }

        if(!list.isEmpty()) {
            return ResponseEntity.ok(list);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/timetable/cached")
    HttpEntity<List<Cache>> cachedData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CacheJson jsonpObject = cacheJsonRepository.findAll().get(0);
        List<Cache> participantJsonList = mapper.readValue(jsonpObject.getJson(), new TypeReference<List<Cache>>(){});
        return ResponseEntity.ok(participantJsonList);

    }
}
