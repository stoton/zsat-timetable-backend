package com.github.stoton.tools;

import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.domain.DaysEnum;
import com.github.stoton.domain.Lesson;
import com.github.stoton.domain.Subentry;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void removeHtmlTagsWhenTagsAreInCorrectFormTest() {
        String given = "<p>Kimi no na wa?</p>";

        String actual = Utils.removeHtmlTags(given);

        String expected = "Kimi no na wa?";

        assertEquals(expected, actual);
    }

    @Test
    public void removeHtmlTagsWhenTagsAreInIncorrectFormTest() {
        String given = "<p>testcase</b>";

        String actual = Utils.removeHtmlTags(given);

        String expected = "testcase";

        assertEquals(expected, actual);
    }

    @Test
    public void deleteTextWithinBracketsWhenTextIsAroundBracketsTest() {
        String given = "(anothercasetest)aa";

        String actual = Utils.deleteTextWithinBrackets(given);

        String expected = "aa";

        assertEquals(expected, actual);
    }

    @Test
    public void deleteTextWithinBracketsWhenTextIsOnlyInBracketsTest() {
        String given = "(anothercasetest)";

        String actual = Utils.deleteTextWithinBrackets(given);

        String expected = "";

        assertEquals(expected, actual);
    }

    @Test
    public void startsWithUppercaseAndDotWhenStringStartWithUppercaseAndDotTest() {
        String given = "T.estcase";

        boolean actual = Utils.startsWithUppercaseAndDot(given);

        assertTrue(actual);
    }

    @Test
    public void startsWithUppercaseAndDotWhenStringStartWithUppercaseTest() {
        String given = "Testcase";

        boolean actual = Utils.startsWithUppercaseAndDot(given);

        assertFalse(actual);
    }

    @Test
    public void startsWithUppercaseAndDotWhenStringStartWithDotTest() {
        String given = ".Testcase";

        boolean actual = Utils.startsWithUppercaseAndDot(given);

        assertFalse(actual);
    }

    @Test
    public void filterTestWhenStringContainsPattern() {
        String pattern = "case";

        String given = "testcase";

        String actual = Utils.filter(pattern, given);

        String expected = "testcase";

        assertEquals(expected, actual);
    }

    @Test
    public void filterTestWhenStringNotContainsPattern() {
        String pattern = "notcontain";

        String given = "testcase";

        String actual = Utils.filter(pattern, given);

        String expected = "";

        assertEquals(expected, actual);
    }

    @Test
    public void addSpaceToIndexWhenIndexIsInRangeTest() {
        String given = "testcase";

        String actual = Utils.addSpaceToIndex(given, 3);

        String expected = "test case";

        assertEquals(expected, actual);

    }

    @Test
    public void addSpaceToIndexWhenIndexIsNotInRangeTest() {
        String given = "testcase";

        String actual = Utils.addSpaceToIndex(given, -1);

        String expected = "testcase";

        assertEquals(expected, actual);
    }

    @Test
    public void extractElementsByRegexWithDigitsTest() {
        String given = "12ad4n23";

        List<String> actual = Utils.extractElementsByRegex(given, "\\d");

        List<String> expected = new ArrayList<>(Arrays.asList("1", "2", "4", "2", "3"));

        assertEquals(expected, actual);
    }

    @Test
    public void extractElementsByRegexWithNumbersTest() {
        String given = "12ad4n23";

        List<String> actual = Utils.extractElementsByRegex(given, "[0-9]+");

        List<String> expected = new ArrayList<>(Arrays.asList("12", "4", "23"));

        assertEquals(expected, actual);
    }

    @Test
    public void extractElementsByRegexWithSomeWhitespacesTest() {
        String given = "Ala ma kota a kot   ma  Ale";

        List<String> actual = Utils.extractElementsByRegex(given, "[a-zA-Z]+");

        List<String> expected = new ArrayList<>(Arrays.asList("Ala", "ma", "kota", "a", "kot", "ma", "Ale"));

        assertEquals(expected, actual);
    }

    @Test
    public void isOverRangeWhenNumberIsInRangeTest() {
        int given = 4;

        int range = 7;

        boolean actual = Utils.isOverRange(given, range);

        assertFalse(actual);
    }

    @Test
    public void isOverRangeWhenNumberIsOverRangeTest() {
        int given = 7;

        int range = 4;

        boolean actual = Utils.isOverRange(given, range);

        assertTrue(actual);
    }

    @Test
    public void addLessonToDayTest() {

        List<Subentry> mondaySubentry = new ArrayList<>();

        mondaySubentry.add(new Subentry("pra.w ob.at", "2 Tia-2/2", "216"));

        Lesson mondayLesson = Lesson
                .builder()
                .lessonNumber("7")
                .timePhase("13:15-14:00")
                .subentries(mondaySubentry)
                .build();

        CompleteTimetable completeTimetable = CompleteTimetable
                .builder()
                .monday(new ArrayList<>())
                .tuesday(new ArrayList<>())
                .wednesday(new ArrayList<>())
                .thursday(new ArrayList<>())
                .friday(new ArrayList<>())
                .build();

        Utils.addLessonToDay(completeTimetable, mondayLesson, DaysEnum.MONDAY);

        int actual = completeTimetable
                .getMonday()
                .size();

        int expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    public void deleteEmptyLessonFromTopWhenSomeLessonsAreEmptyTest() {
        List<Subentry> mondaySubentry = new ArrayList<>();

        List<Subentry> mondayEmptySubentry = new ArrayList<>();
        mondaySubentry.add(new Subentry("pra.w ob.at", "2 Tia-2/2", "216"));


        Lesson mondayFirstLesson = Lesson
                .builder()
                .lessonNumber("1")
                .timePhase("08:00-08:45")
                .subentries(mondaySubentry)
                .build();

        Lesson mondaySecondLesson = Lesson
                .builder()
                .lessonNumber("2")
                .timePhase("08:50-09:35")
                .subentries(mondayEmptySubentry)
                .build();

        CompleteTimetable completeTimetable = CompleteTimetable
                .builder()
                .monday(new ArrayList<>())
                .tuesday(new ArrayList<>())
                .wednesday(new ArrayList<>())
                .thursday(new ArrayList<>())
                .friday(new ArrayList<>())
                .build();

        Utils.addLessonToDay(completeTimetable, mondayFirstLesson, DaysEnum.MONDAY);
        Utils.addLessonToDay(completeTimetable, mondaySecondLesson, DaysEnum.MONDAY);

        Utils.deleteEmptyLessonFromTop(completeTimetable);


        int expected = 1;

        int actual = completeTimetable.getMonday().size();

        assertEquals(expected, actual);
    }

    @Test
    public void deleteEmptyLessonFromTopWhenNoneExistsTest() {
        List<Subentry> mondayFirstSubentry = new ArrayList<>();

        List<Subentry> mondaySecondSubentry = new ArrayList<>();

        mondayFirstSubentry.add(new Subentry("pra.w ob.at", "2 Tia-2/2", "216"));
        mondaySecondSubentry.add(new Subentry("pra.w ob.at", "2 Tia-2/2", "216"));


        Lesson mondayFirstLesson = Lesson
                .builder()
                .lessonNumber("1")
                .timePhase("08:00-08:45")
                .subentries(mondayFirstSubentry)
                .build();

        Lesson mondaySecondLesson = Lesson
                .builder()
                .lessonNumber("2")
                .timePhase("08:50-09:35")
                .subentries(mondaySecondSubentry)
                .build();

        CompleteTimetable completeTimetable = CompleteTimetable
                .builder()
                .monday(new ArrayList<>())
                .tuesday(new ArrayList<>())
                .wednesday(new ArrayList<>())
                .thursday(new ArrayList<>())
                .friday(new ArrayList<>())
                .build();

        Utils.addLessonToDay(completeTimetable, mondayFirstLesson, DaysEnum.MONDAY);
        Utils.addLessonToDay(completeTimetable, mondaySecondLesson, DaysEnum.MONDAY);

        Utils.deleteEmptyLessonFromTop(completeTimetable);


        int expected = 2;

        int actual = completeTimetable.getMonday().size();

        assertEquals(expected, actual);
    }

    @Test
    public void deleteAllWhitespacesWithLeadingZeroWhenThereIsSingleSpaceTest() {
        String given = "0 3:11";

        String expected = "03:11";

        String actual = Utils.deleteAllWhitespacesWithLeadingZero(given);

        assertEquals(expected, actual);
    }

    @Test
    public void deleteAllWhitespacesWithLeadingZeroWhenThereIsMultipleWhitespacesTest() {
        String given = "0                            3:11";

        String expected = "03:11";

        String actual = Utils.deleteAllWhitespacesWithLeadingZero(given);

        assertEquals(expected, actual);
    }

    @Test
    public void deleteAllWhitespacesWithLeadingZeroWhenThereIsNoWhitespacesTest() {
        String given = "03:11";

        String expected = "03:11";

        String actual = Utils.deleteAllWhitespacesWithLeadingZero(given);

        assertEquals(expected, actual);
    }

    @Test
    public void getNextDayOfWeekWhenCurrentDayIsMondayTest() {
        DaysEnum given = DaysEnum.MONDAY;

        DaysEnum expected = DaysEnum.TUESDAY;

        DaysEnum actual = Utils.getNextDayOfWeek(given);

        assertEquals(expected, actual);
    }

    @Test
    public void getNextDayOfWeekWhenCurrentDayIsFridayTest() {
        DaysEnum given = DaysEnum.FRIDAY;

        DaysEnum expected = DaysEnum.MONDAY;

        DaysEnum actual = Utils.getNextDayOfWeek(given);

        assertEquals(expected, actual);
    }
}
