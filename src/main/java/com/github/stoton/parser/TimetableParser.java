package com.github.stoton.parser;

import com.github.stoton.domain.DayContainer;
import com.github.stoton.repository.TimetableIndexItemRepository;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;

public interface TimetableParser {
    DayContainer parseDocument(Document document, TimetableIndexItemRepository timetableIndexItemRepository) throws ParseException, IOException;
}