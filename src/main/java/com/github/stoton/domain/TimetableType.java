package com.github.stoton.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TimetableType {

    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    CLASSROOM("CLASSROOM");

    private final String name;

    public static TimetableType parseTimetableType(String name) {
        for (TimetableType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown timetable type: " + name);
    }

    @Override
    public String toString() {
        return name;
    }
}
