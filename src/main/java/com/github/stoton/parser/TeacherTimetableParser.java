package com.github.stoton.parser;

import com.github.stoton.domain.*;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.tools.Pair;
import com.github.stoton.tools.Utils;
import com.google.common.collect.Iterables;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class TeacherTimetableParser implements TimetableParser {

    private static final String URL_ROOT = "http://szkola.zsat.linuxpl.eu/planlekcji/plany/";

    @Override
    public CompleteTimetable parseDocument(Document document, TimetableIndexItemRepository timetableIndexItemRepository) throws ParseException, IOException {

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

            List<Pair<String, String> > pairsOfHyperlinkWithNameOfLink = new ArrayList<>();

            Elements linksToClassrooms = tableCells.select(CssQuery.LINK_START_WITH_S.toString());

            for (Element link : linksToClassrooms) {
                pairsOfHyperlinkWithNameOfLink.add(
                    new Pair<>(
                        link.text(),
                        link.attr("href")
                    )
                );
            }

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
                    final String[] addon = {classroomsIterator.next().text()};

                    Optional<String> candidateForStudent = secondaryText
                            .stream()
                            .filter(secondaryCandidate -> !Objects.equals(Utils.filter(second, secondaryCandidate), ""))
                            .findFirst();

                    String filteredStudent = candidateForStudent.orElse("");

                    if("".equals(filteredStudent)) {
                        subentry.setSecondaryText(Utils.addSpaceToIndex(second, 0));
                    } else {
                        subentry.setSecondaryText(Utils.addSpaceToIndex(filteredStudent, 0));
                    }

                    subentry.setPrimaryText(primary);

                    String finalAddon = addon[0];

                    pairsOfHyperlinkWithNameOfLink
                            .parallelStream()
                            .filter(pair -> finalAddon.contains(pair.getFirst()))
                            .findFirst()
                            .ifPresent((pair) -> {
                                try {
                                    Document doc = Jsoup.connect(URL_ROOT + pair.getSecond()).get();
                                    addon[0] = Utils.parseName(Utils.extractTitle(doc));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                    subentry.setAddon(addon[0]);
                    lesson.getSubentries().add(subentry);
                });

                Utils.addLessonToDay(completeTimetable, lesson, currentDay);
                currentDay = Utils.getNextDayOfWeek(currentDay);
            }
        }

        Utils.deleteEmptyLessonFromTop(completeTimetable);

        return completeTimetable;
    }
}
