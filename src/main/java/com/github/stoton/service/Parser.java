package com.github.stoton.service;


import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.domain.TimetableIndexItem;

import java.io.IOException;
import java.util.List;

public interface Parser {
    CompleteTimetable parseDataFromZsat(String url, String type);

    List<TimetableIndexItem> parseDataFromZsatTimetableIndex() throws IOException;

}
