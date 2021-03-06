package com.github.stoton.parser;

import com.github.stoton.domain.TimetableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ParserFactory {

    @Autowired
    private List<TimetableParser> parsers;

    private static final Map<TimetableType, TimetableParser> parsersCache = new HashMap<>();

    @PostConstruct
    public void initParsersCache() {
        for (TimetableParser service : parsers) {
            parsersCache.put(service.getType(), service);
        }
    }

    public static TimetableParser createParser(TimetableType type) {

        Optional<TimetableParser> parser = Optional.ofNullable(parsersCache.get(type));

        if (parser.isPresent()) {
            return parser.get();
        }

        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
