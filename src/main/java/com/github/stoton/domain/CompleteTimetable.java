package com.github.stoton.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CompleteTimetable {

    private List<Lesson> monday;
    private List<Lesson> tuesday;
    private List<Lesson> wednesday;
    private List<Lesson> thursday;
    private List<Lesson> friday;
}
