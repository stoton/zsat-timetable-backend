package com.github.stoton.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stoton.domain.Cache;
import com.github.stoton.domain.CacheJson;
import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.domain.TimetableIndexItem;
import com.github.stoton.repository.CacheJsonRepository;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.parser.Parser;
import com.github.stoton.tools.AppProperties;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class TimetableItemController {

    private Parser parser;

    private TimetableIndexItemRepository timetableIndexItemRepository;

    private CacheJsonRepository cacheJsonRepository;

    @Autowired
    public TimetableItemController(Parser parser, TimetableIndexItemRepository timetableIndexItemRepository, CacheJsonRepository cacheJsonRepository) {
        this.parser = parser;
        this.timetableIndexItemRepository = timetableIndexItemRepository;
        this.cacheJsonRepository = cacheJsonRepository;
    }

    @GetMapping(value = "/timetable/{name:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<CompleteTimetable> parseTimetable(@PathVariable String name) throws IOException, ParseException {

        Optional<TimetableIndexItem> timetableIndexItem = Optional.ofNullable(timetableIndexItemRepository.findFirstByName(name));

        if(!timetableIndexItem.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String url = AppProperties.ZSAT_TIMETABLE_ROOT_URL + timetableIndexItem.get().getUrl();

        Optional<CompleteTimetable> completeTimetable = Optional.ofNullable(parser.parseZsatDocument(url, timetableIndexItem.get().getType()));

        return completeTimetable.<HttpEntity<CompleteTimetable>>
                    map(timetable -> ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)
                    .cachePrivate())
                    .body(timetable))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/timetable", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<TimetableIndexItem>> getTimetableIndexItems() throws IOException {
        List<TimetableIndexItem> timetableIndexItems = parser.parseDataFromZsatTimetableIndex();

        timetableIndexItemRepository.deleteAll();

        for(TimetableIndexItem timetableIndexItem : timetableIndexItems) {
            Optional<TimetableIndexItem> current = Optional.ofNullable(timetableIndexItemRepository.findFirstByName(timetableIndexItem.getName()));

            if(!current.isPresent()) {
                timetableIndexItemRepository.save(timetableIndexItem);
            }
        }

        if(!timetableIndexItems.isEmpty()) {
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePrivate())
                    .body(timetableIndexItems);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/timetable/cached", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<List<Cache>> cachedData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Optional<CacheJson> cache = cacheJsonRepository.findAll()
                .stream()
                .findFirst();

        if(cache.isPresent()) {
            List<Cache> participantJsonList = mapper.readValue(cache.get().getJson(), new TypeReference<List<Cache>>(){});
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePrivate())
                    .body(participantJsonList);
        } else {
            return ResponseEntity.notFound()
                    .build();

        }
    }
}
