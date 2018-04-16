package com.github.stoton.parser;


import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.domain.TimetableIndexItem;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface Parser {

    CompleteTimetable parseZsatDocument(String url, String type) throws ParseException, IOException;

    List<TimetableIndexItem> parseDataFromZsatTimetableIndex() throws IOException;
}
