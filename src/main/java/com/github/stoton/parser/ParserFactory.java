package com.github.stoton.parser;

import com.github.stoton.domain.TimetableType;

public class ParserFactory {

    public static TimetableParser createParser(TimetableType type) {
        if(type == TimetableType.STUDENT) {
            return new StudentTimetableParser();
        }

        if(type == TimetableType.CLASSROOM) {
            return new ClassroomTimetableParser();
        }

        if(type == TimetableType.TEACHER) {
            return new TeacherTimetableParser();
        }

        throw  new IllegalStateException("Unknown type: " + type);
    }
}
