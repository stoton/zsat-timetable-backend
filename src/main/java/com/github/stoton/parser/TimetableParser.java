package com.github.stoton.parser;

import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.domain.TimetableType;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;

public interface TimetableParser {
    TimetableType getType();

    CompleteTimetable parseDocument(Document document) throws ParseException, IOException;
}