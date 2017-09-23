package com.github.stoton.domain;

public class Cache {

    private String name;
    private DayContener timetable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DayContener getTimetable() {
        return timetable;
    }

    public void setTimetable(DayContener timetable) {
        this.timetable = timetable;
    }

}
