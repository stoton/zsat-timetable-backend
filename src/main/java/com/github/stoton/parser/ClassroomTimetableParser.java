package com.github.stoton.parser;

import com.github.stoton.domain.*;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.tools.Utils;
import com.google.common.collect.Iterables;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class ClassroomTimetableParser implements TimetableParser {

    private TimetableIndexItemRepository timetableIndexItemRepository;

    @Autowired
    public ClassroomTimetableParser(TimetableIndexItemRepository timetableIndexItemRepository) {
        this.timetableIndexItemRepository = timetableIndexItemRepository;
    }

    @Override
    public TimetableType getType() {
        return TimetableType.CLASSROOM;
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

        for(Element tableRow : tableRows) {

            Elements tableCells = tableRow.select(CssQuery.L_CLASS.toString());

            String numberLesson = tableRow.select(CssQuery.NR_CLASS.toString()).text();
            String timePhase = tableRow.select(CssQuery.HOUR_CLASS.toString()).text();

            timePhase = Utils.deleteAllWhitespacesWithLeadingZero(timePhase);

            for (Element tableData : tableCells) {
                List<String> secondaryText;

                Lesson lesson = Lesson
                        .builder()
                        .lessonNumber(numberLesson)
                        .timePhase(timePhase)
                        .subentries(new ArrayList<>())
                        .build();

                secondaryText = Utils.extractElementsByRegex(tableCells.text(), RegexQuery.EXTRACT_STRING_BY_CLASS.toString());

                Elements subjects = tableData.select(CssQuery.P_CLASS.toString());
                Elements teachers = tableData.select(CssQuery.N_CLASS.toString());
                Elements classrooms = tableData.select(CssQuery.O_CLASS.toString());

                ListIterator<Element> classroomsIterator = classrooms.listIterator();
                ListIterator<Element> teachersIterator = teachers.listIterator();

                subjects.forEach(subject -> {
                    String classroom = classroomsIterator.next().text();

                    Subentry subentry = new Subentry();

                    subentry.setPrimaryText(subject.text());

                    TimetableIndexItem timetableIndexItem = timetableIndexItemRepository
                            .findFirstByTeacherID(teachersIterator.next().text());

                    Optional<TimetableIndexItem> timetableIndexItemOptional = Optional.ofNullable(timetableIndexItem);

                    timetableIndexItemOptional.ifPresent(indexItem -> subentry.setSecondaryText(indexItem.getName()));

                    Optional<String> candidateForClassroom = secondaryText
                            .stream()
                            .filter(secondaryCandidate -> !Objects.equals(Utils.filter(classroom, secondaryCandidate), ""))
                            .findFirst();

                    String filteredClassroom = candidateForClassroom.orElse("");

                    if(filteredClassroom.isEmpty()) {
                        subentry.setAddon(Utils.addSpaceToIndex(classroom, 0));
                    } else {
                        subentry.setAddon(Utils.addSpaceToIndex(filteredClassroom, 0));
                    }
                    lesson.addSubentry(subentry);
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
