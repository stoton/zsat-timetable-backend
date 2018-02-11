package com.github.stoton.parser;

import com.github.stoton.domain.*;
import com.github.stoton.tools.Utils;
import com.google.common.collect.Iterables;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeacherTimetableParser implements TimetableParser {

    @Override
    public TimetableType getType() {
        return TimetableType.TEACHER;
    }

    @Override
    public CompleteTimetable parseDocument(Document document) {

        DaysEnum currentDay = DaysEnum.MONDAY;

        Elements table = document.select(CssQuery.HTML_TABLE_CLASS.toString());

        List<Lesson> monday = new ArrayList<>();
        List<Lesson> tuesday = new ArrayList<>();
        List<Lesson> wednesday = new ArrayList<>();
        List<Lesson> thursday = new ArrayList<>();
        List<Lesson> friday = new ArrayList<>();

        CompleteTimetable completeTimetable = CompleteTimetable
                .builder()
                .monday(monday)
                .tuesday(tuesday)
                .wednesday(wednesday)
                .thursday(thursday)
                .friday(friday)
                .build();

        Iterable<Element> tableRows = Iterables.skip(table.select(CssQuery.TR_ELEMENT.toString()), 1);

        for (Element tableRow : tableRows) {

            Elements tableCells = tableRow.select(CssQuery.L_CLASS.toString());

            String lessonNumber = tableRow.select(CssQuery.NR_CLASS.toString()).text();
            String timePhase = tableRow.select(CssQuery.HOUR_CLASS.toString()).text();

            timePhase = Utils.deleteAllWhitespacesWithLeadingZero(timePhase);

            for (Element tableData : tableCells) {
                List<String> secondaryText;

                Lesson lesson = Lesson
                        .builder()
                        .lessonNumber(lessonNumber)
                        .timePhase(timePhase)
                        .subentries(new ArrayList<>())
                        .build();

                secondaryText = Utils.extractElementsByRegex(tableCells.text(), RegexQuery.EXTRACT_STRING_BY_CLASS.toString());

                Elements subjects = tableData.select(CssQuery.P_CLASS.toString());
                Elements students = tableData.select(CssQuery.O_CLASS.toString());
                Elements classrooms = tableData.select(CssQuery.S_CLASS.toString());

                ListIterator<Element> studentsIterator = students.listIterator();
                ListIterator<Element> classroomsIterator = classrooms.listIterator();

                subjects.forEach(subject -> {
                    Subentry subentry = new Subentry();

                    String primary = subject.text();
                    String second = studentsIterator.next().text();
                    String addon = classroomsIterator.next().text();

                    Optional<String> candidateForStudent = secondaryText
                            .stream()
                            .filter(secondaryCandidate -> !Objects.equals(Utils.filter(second, secondaryCandidate), ""))
                            .findFirst();

                    String filteredStudent = candidateForStudent.orElse("");

                    if (filteredStudent.isEmpty()) {
                        subentry.setSecondaryText(Utils.addSpaceToIndex(second, 0));
                    } else {
                        subentry.setSecondaryText(Utils.addSpaceToIndex(filteredStudent, 0));
                    }

                    subentry.setPrimaryText(primary);

                    subentry.setAddon(addon);
                    lesson.getSubentries().add(subentry);
                });

                if (currentDay != null) {
                    Utils.addLessonToDay(completeTimetable, lesson, currentDay);
                    currentDay = Utils.getNextDayOfWeek(currentDay);
                }

            }
        }

        Utils.deleteEmptyLessonFromTop(completeTimetable);

        return completeTimetable;
    }
}
