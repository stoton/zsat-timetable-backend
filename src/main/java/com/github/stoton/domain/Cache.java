package com.github.stoton.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cache {
    private String name;
    private CompleteTimetable timetable;
}
