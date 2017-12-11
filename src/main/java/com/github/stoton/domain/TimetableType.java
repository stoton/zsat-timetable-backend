package com.github.stoton.domain;

public enum TimetableType {
    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    CLASSROOM("CLASSROOM");

    private final String name;

    TimetableType(String name) {
        this.name = name;
    }

    public static TimetableType parseTimetableType(String name) {

        if(name.equals(STUDENT.toString())) {
            return STUDENT;
        }

        if(name.equals(TEACHER.toString())) {
            return TEACHER;
        }

        if(name.equals(CLASSROOM.toString())) {
            return CLASSROOM;
        }

        throw new IllegalStateException("Unknown timetable type: " + name);
    }

    @Override
    public String toString() {
        return name;
    }
}
