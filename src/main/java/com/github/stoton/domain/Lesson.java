package com.github.stoton.domain;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Lesson {

    private String lessonNumber;
    private String timePhase;
    private List<Subentry> subentries = new ArrayList<>();

    public void addSubentry(Subentry subentry) {
        subentries.add(subentry);
    }
}

