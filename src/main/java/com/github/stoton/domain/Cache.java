package com.github.stoton.domain;

public class Cache {

    private String name;
    private DayContainer timetable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DayContainer getTimetable() {
        return timetable;
    }

    public void setTimetable(DayContainer timetable) {
        this.timetable = timetable;
    }

}
