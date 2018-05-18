package com.github.stoton.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompleteTimetable {

    private List<Lesson> monday;
    private List<Lesson> tuesday;
    private List<Lesson> wednesday;
    private List<Lesson> thursday;
    private List<Lesson> friday;
}
