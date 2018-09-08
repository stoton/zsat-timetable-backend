package com.github.stoton.parser;

import com.github.stoton.domain.*;
import com.github.stoton.tools.Utils;
import com.google.common.collect.Iterables;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TeacherTimetableParser implements TimetableParser {

    private TimetableStrategyUtils timetableStrategyUtils = new TimetableStrategyUtils();

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

                if (isSpecialCase(tableData)) {



                    String fixedHtml = fixHtml(tableData.html());


                    Lesson lesson = Lesson
                            .builder()
                            .lessonNumber(lessonNumber)
                            .timePhase(timePhase)
                            .subentries(new ArrayList<>())
                            .build();


                    Elements students = Jsoup.parse(fixedHtml).getElementsByClass("o");

                    String addon =  tableData.getElementsByClass("s").first().text();
                    String subject = tableData.getElementsByClass("p").first().text();
                    for (Element student : students) {
                        Subentry subentry = new Subentry();
                        subentry.setPrimaryText(subject);
                        subentry.setAddon(addon);
                        subentry.setSecondaryText(Utils.addSpaceToIndex(student.text(), 0));
                        lesson.getSubentries().add(subentry);

                    }
                    if (currentDay != null) {
                        Utils.addLessonToDay(completeTimetable, lesson, currentDay);
                        currentDay = Utils.getNextDayOfWeek(currentDay);
                    }


                } else {

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
        }

        Utils.deleteEmptyLessonFromTop(completeTimetable);

        return completeTimetable;
    }

    private boolean isSpecialCase(Element row) {
        return row.getElementsByClass("s").size() == 1
                && row.getElementsByClass("p").size() == 1;
    }

    String fixHtml(String html) {

        int indexOfElementToMerge = TimetableStrategyUtils.NO_OCCURRENCE;

        Pattern pattern = Pattern.compile(TimetableStrategyUtils.OUTER_PART_OF_CLASS);
        Matcher matcher = pattern.matcher(html);

        while (!isHtmlValid(html)) {
            StringBuilder correctHtml = new StringBuilder();

            if (matcher.find()) {
                indexOfElementToMerge = html.indexOf(matcher.group()) + 1;

            }

            if (indexOfElementToMerge == TimetableStrategyUtils.NO_OCCURRENCE) break;

            correctHtml
                    .append(timetableStrategyUtils.appendHtmlUntilPartToMerge(html, indexOfElementToMerge - 4))
                    .append(timetableStrategyUtils.appendPartOfClassToMerge(html, indexOfElementToMerge, indexOfElementToMerge + 5))
                    .append(timetableStrategyUtils.appendEndOfSpan(html, indexOfElementToMerge - 4, indexOfElementToMerge))
                    .append(timetableStrategyUtils.appendEndOfHtml(html, indexOfElementToMerge + 5));

            html = correctHtml.toString();
        }
        html = html.replaceAll(",", "");

        return html;
    }

    public boolean isHtmlValid(String html) {
        return !hasClassPartOutOfTag(html);
    }

    private class TimetableStrategyUtils {

        static final String OUTER_PART_OF_CLASS = ">-[0-9]/[0-9]";
        static final String SUBJECT_CLASS = "class=\"p\"";
        static final String STUDENT_CLASS = "class=\"o\"";
        static final String TEACHER_CLASS = "class=\"n\"";
        static final String ROOM_CLASS = "class=\"s\"";
        static final int NO_OCCURRENCE = -1;


        String appendHtmlUntilPartToMerge(String html, int end) {
            return html.substring(0, end);
        }

        String appendPartOfClassToMerge(String html, int start, int end) {
            return html.substring(start, end);
        }

        String appendEndOfSpan(String html, int start, int end) {
            return html.substring(start, end);
        }

        String appendEndOfHtml(String html, int start) {
            return html.substring(start);
        }

    }

    boolean hasClassPartOutOfTag(String html) {
        Pattern pattern = Pattern.compile(TimetableStrategyUtils.OUTER_PART_OF_CLASS);
        Matcher matcher = pattern.matcher(html);

        return matcher.find();
    }
}
