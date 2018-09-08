package com.github.stoton.parser.test;


import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.domain.Lesson;
import com.github.stoton.domain.Subentry;
import com.github.stoton.domain.TimetableIndexItem;
import com.github.stoton.parser.ClassroomTimetableParser;
import com.github.stoton.parser.StudentTimetableParser;
import com.github.stoton.parser.TeacherTimetableParser;
import com.github.stoton.parser.TimetableParser;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimetableParserTest {

    private TimetableParser timetableParser;

    @Mock
    private TimetableIndexItemRepository timetableIndexItemRepository;

    @Test
    public void teacherParserTestWhenDocumentIsCorrect() throws IOException, ParseException {

        timetableParser = new TeacherTimetableParser();

        List<Subentry> mondaySubentries = new ArrayList<>();

        mondaySubentries.add(new Subentry("pra.w ob.at", "2 Tia-2/2", "216"));

        Lesson mondayLesson = Lesson
                .builder()
                .lessonNumber("7")
                .timePhase("13:15-14:00")
                .subentries(mondaySubentries)
                .build();

        CompleteTimetable expected = CompleteTimetable
                .builder()
                .monday(new ArrayList<>(Collections.singletonList(mondayLesson)))
                .tuesday(Collections.emptyList())
                .wednesday(Collections.emptyList())
                .thursday(Collections.emptyList())
                .friday(Collections.emptyList())
                .build();

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream file = classLoader.getResourceAsStream("correctTeacherTimetable.html");

        String html = CharStreams.toString(new InputStreamReader(
                file, Charsets.UTF_8));

        Document document = Jsoup.parse(html);

        CompleteTimetable actual = timetableParser.parseDocument(document);

        assertEquals(expected, actual);
    }

    @Test
    public void studentParserTestWhenDocumentIsCorrect() throws IOException, ParseException {

        timetableParser = new StudentTimetableParser(timetableIndexItemRepository);

        when(timetableIndexItemRepository.findFirstByTeacherID("KD"))
                .thenReturn(new TimetableIndexItem("M. Kędzior", "TEACHER", "plany/n22.html", "http://szkola.zsat.linuxpl.eu/planlekcji/plany/n22.html"));

        List<Subentry> mondaySubentries = new ArrayList<>();

        mondaySubentries.add(new Subentry("religia", "M. Kędzior", "206"));

        Lesson mondayLesson = Lesson
                .builder()
                .lessonNumber("4")
                .timePhase("10:45-11:30")
                .subentries(mondaySubentries)
                .build();

        CompleteTimetable expected = CompleteTimetable
                .builder()
                .monday(new ArrayList<>(Collections.singletonList(mondayLesson)))
                .tuesday(Collections.emptyList())
                .wednesday(Collections.emptyList())
                .thursday(Collections.emptyList())
                .friday(Collections.emptyList())
                .build();

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream file = classLoader.getResourceAsStream("correctStudentTimetable.html");

        String html = CharStreams.toString(new InputStreamReader(
                file, Charsets.UTF_8));

        Document document = Jsoup.parse(html);

        CompleteTimetable actual = timetableParser.parseDocument(document);

        assertEquals(expected, actual);
    }

    @Test
    public void classroomParserTestWhenDocumentIsCorrect() throws IOException, ParseException {

        timetableParser = new ClassroomTimetableParser(timetableIndexItemRepository);

        when(timetableIndexItemRepository.findFirstByTeacherID("RF"))
                .thenReturn(new TimetableIndexItem("R. Feret", "TEACHER", "plany/n16.html", "http://szkola.zsat.linuxpl.eu/planlekcji/plany/n16.html"));


        List<Subentry> mondaySubentries = new ArrayList<>();

        mondaySubentries.add(new Subentry("informatyka", "R. Feret", "1 Ti-2/2"));

        Lesson lesson = Lesson
                .builder()
                .lessonNumber("3")
                .timePhase("09:40-10:25")
                .subentries(mondaySubentries)
                .build();

        CompleteTimetable expected = CompleteTimetable
                .builder()
                .monday(new ArrayList<>(Collections.singletonList(lesson)))
                .tuesday(Collections.emptyList())
                .wednesday(Collections.emptyList())
                .thursday(Collections.emptyList())
                .friday(Collections.emptyList())
                .build();

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream file = classLoader.getResourceAsStream("correctClassroomTimetable.html");

        String html = CharStreams.toString(new InputStreamReader(
                file, Charsets.UTF_8));

        Document document = Jsoup.parse(html);

        CompleteTimetable actual = timetableParser.parseDocument(document);

        assertEquals(expected, actual);
    }

    @Test
    public void teacherParserTestWhenDocumentIsInCorrect() throws IOException, ParseException {

        timetableParser = new TeacherTimetableParser();

        List<Subentry> mondaySubentries = new ArrayList<>();

        mondaySubentries.add(new Subentry("wf", "4 Tż-2/2", "@"));
        mondaySubentries.add(new Subentry("wf", "4 TIG-2/3", "@"));
        mondaySubentries.add(new Subentry("wf", "4 TAŻ-1/2", "@"));

        Lesson mondayLesson = Lesson
                .builder()
                .lessonNumber("7")
                .timePhase("13:15-14:00")
                .subentries(mondaySubentries)
                .build();

        CompleteTimetable expected = CompleteTimetable
                .builder()
                .monday(new ArrayList<>(Collections.singletonList(mondayLesson)))
                .tuesday(Collections.emptyList())
                .wednesday(Collections.emptyList())
                .thursday(Collections.emptyList())
                .friday(Collections.emptyList())
                .build();

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream file = classLoader.getResourceAsStream("inCorrectTeacherTimetable.html");

        String html = CharStreams.toString(new InputStreamReader(
                file, Charsets.UTF_8));

        Document document = Jsoup.parse(html);

        CompleteTimetable actual = timetableParser.parseDocument(document);

        assertEquals(expected, actual);
    }
}
