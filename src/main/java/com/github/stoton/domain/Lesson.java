package com.github.stoton.domain;

import java.util.ArrayList;
import java.util.List;

public class Lesson {

    private String lessonNumber;
    private String timePhase;
    private List<Subentry> subentries = new ArrayList<>();

    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getTimePhase() {
        return timePhase;
    }

    public void setTimePhase(String timePhase) {
        this.timePhase = timePhase;
    }

    public List<Subentry> getSubentries() {
        return subentries;
    }

    public void setSubentries(List<Subentry> subentries) {
        this.subentries = subentries;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                ", lessonNumber='" + lessonNumber + '\'' +
                ", timePhase='" + timePhase + '\'' +
                ", subentries=" + subentries +
                '}';
    }
}

