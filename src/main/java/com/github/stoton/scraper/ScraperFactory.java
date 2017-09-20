package com.github.stoton.scraper;

import com.github.stoton.domain.TimetableType;

public class ScraperFactory {

    public static TimetableScraper createParser(TimetableType type) {
        if(type == TimetableType.STUDENT) {
            return new StudentTimetableScraper();
        }

        if(type == TimetableType.CLASSROOM) {
            return new ClassroomTimetableScraper();
        }

        if(type == TimetableType.TEACHER) {
            return new TeacherTimetableScraper();
        }

        throw  new IllegalStateException("Unkown type: " + type);
    }
}
