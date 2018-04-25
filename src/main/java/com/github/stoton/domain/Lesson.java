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
public class Lesson {

    private String lessonNumber;
    private String timePhase;
    private List<Subentry> subentries;

    public void addSubentry(Subentry subentry) {
        subentries.add(subentry);
    }
}

