package com.github.stoton.parser;

import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.repository.TimetableIndexItemRepository;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;

public interface TimetableParser {
    CompleteTimetable parseDocument(Document document, TimetableIndexItemRepository timetableIndexItemRepository) throws ParseException, IOException;
}