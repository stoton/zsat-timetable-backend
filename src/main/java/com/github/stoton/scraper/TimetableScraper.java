package com.github.stoton.scraper;

import com.github.stoton.domain.DayContener;
import com.github.stoton.repository.TimetableIndexItemRepository;
import org.jsoup.nodes.Document;

import java.text.ParseException;

public interface TimetableScraper {
    DayContener parseDocument(Document document, TimetableIndexItemRepository timetableIndexItemRepository) throws ParseException;
}