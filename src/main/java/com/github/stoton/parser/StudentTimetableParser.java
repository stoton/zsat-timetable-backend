package com.github.stoton.parser;

import com.github.stoton.domain.*;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.tools.Utils;
import com.google.common.collect.Iterables;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
public class StudentTimetableParser implements TimetableParser {

    private TimetableIndexItemRepository timetableIndexItemRepository;

    @Autowired
    public StudentTimetableParser(TimetableIndexItemRepository timetableIndexItemRepository) {
        this.timetableIndexItemRepository = timetableIndexItemRepository;
    }

    @Override
    public TimetableType getType() {
        return TimetableType.STUDENT;
    }

    @Override
    public CompleteTimetable parseDocument(Document document) throws ParseException, IOException {

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

        for(Element tableRow : tableRows) {

            Elements tableCells = tableRow.select(CssQuery.L_CLASS.toString());

            String lessonNumber = tableRow.select(CssQuery.NR_CLASS.toString()).text();
            String timePhase = tableRow.select(CssQuery.HOUR_CLASS.toString()).text();

            timePhase = Utils.deleteAllWhitespacesWithLeadingZero(timePhase);

            for(Element tableData : tableCells) {
                List<String> secondaryText;
                Lesson lesson = Lesson
                        .builder()
                        .lessonNumber(lessonNumber)
                        .timePhase(timePhase)
                        .subentries(new ArrayList<>())
                        .build();

                secondaryText = Utils.extractElementsByRegex(tableCells.text(), RegexQuery.EXTRACT_STRING_BY_CLASS.toString());

                Elements subject = tableData.select(CssQuery.P_CLASS.toString());
                Elements teacher = tableData.select(CssQuery.N_CLASS.toString());
                Elements classroom = tableData.select(CssQuery.S_CLASS.toString());

                ListIterator<Element> classroomsIterator = classroom.listIterator();

                int teacherCounter = 0;
                for (int subjectCounter = 0; subjectCounter < subject.size(); subjectCounter++) {

                    String primary;
                    String second;
                    if(!Utils.isOverRange(subjectCounter+1, subject.size())
                            && subject.get(subjectCounter+1).text().startsWith("#")) {
                        primary = subject.get(subjectCounter).text();
                        subjectCounter++;
                        second = subject.get(subjectCounter).text();
                    } else {
                            TimetableIndexItem timetableIndexItem = timetableIndexItemRepository.findFirstByTeacherID(teacher.get(teacherCounter).text());
                            primary = subject.get(subjectCounter).text();
                            second = timetableIndexItem.getName();
                            teacherCounter++;
                    }

                    String addon = classroomsIterator.next().text();

                    Subentry subentry = new Subentry();

                    Optional<String> candidateForSubject = secondaryText
                            .stream()
                            .filter(candidate -> !Objects.equals(Utils.filter(primary, candidate), ""))
                            .findFirst();

                    String filteredPrimary = candidateForSubject.orElse("");


                    if(filteredPrimary.isEmpty()) {
                        subentry.setPrimaryText(primary);
                    }
                    else {
                        subentry.setPrimaryText(filteredPrimary);
                    }

                    subentry.setSecondaryText(second);
                    subentry.setAddon(addon);

                    lesson.getSubentries().add(subentry);
                }

                Utils.addLessonToDay(completeTimetable, lesson, currentDay);

                currentDay = Utils.getNextDayOfWeek(currentDay);
            }
        }

        Utils.deleteEmptyLessonFromTop(completeTimetable);

        return CompleteTimetable
                .builder()
                .monday(monday)
                .tuesday(tuesday)
                .wednesday(wednesday)
                .thursday(thursday)
                .friday(friday)
                .build();
    }
}
