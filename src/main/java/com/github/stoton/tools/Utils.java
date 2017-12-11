package com.github.stoton.tools;

import com.github.stoton.domain.*;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String parseName(String name) {
        if (name == null) {
            return "";
        }

        name = name.replace(" ", "");
        name = removeHtmlTags(name);
        name = deleteTextWithinBrackets(name);

        if (startsWithUppercaseAndDot(name)) {
            name = name.charAt(0) + ". " + name.substring(2);
        } else if (startsWithOneDigit(name)) {
            name = name.charAt(0) + " " + name.substring(1);
        }

        return name;
    }

    private static String removeHtmlTags(String string) {
        return string.replaceAll("<[\\w/]*>", "");
    }

    private static String deleteTextWithinBrackets(String str) {
        if (str.isEmpty() || Character.isDigit(str.charAt(0))) {
            return str;
        }

        int leftBracket = str.indexOf('(');
        int rightBracket = str.indexOf(')');
        if (leftBracket != -1 && rightBracket != -1) {
            str = str.substring(0, leftBracket) +
                    str.substring(rightBracket + 1);
        }
        return str;
    }

    private static boolean startsWithUppercaseAndDot(String string) {
        String regex = "^([A-Z]|[ĄĆĘŁŃÓŚŻŹ])(\\.)(.)*$";
        return string.matches(regex);
    }

    private static boolean startsWithOneDigit(String string) {
        String regex = "^(\\d)(\\D)(.)*$";
        return string.matches(regex);
    }

    public static String filter(String given, String expected) {
        return expected.contains(given) ? expected : "";
    }

    public static String addSpaceToIndex(String s, int index) {
        StringBuilder newString = new StringBuilder();
        for(int i = 0; i < s.length(); i++) {
            newString.append(s.charAt(i));
            if(i == index)
                newString.append(" ");
        }
        return newString.toString();
    }

    public static List<String> extractElementsByRegex(String elements, String regex) {
        List<String> strings = new ArrayList<>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(elements);

        while(matcher.find()) {
            strings.add(matcher.group());
        }

        return strings;
    }

    public static boolean isOverRange(int number, int range) {
        return !(number < range);
    }

    public static void addLessonToDay(CompleteTimetable completeTimetable, Lesson lesson, DaysEnum currentDay) {

        switch (currentDay) {
            case MONDAY:
                completeTimetable.getMonday().add(lesson);
                break;
            case TUESDAY:
                completeTimetable.getTuesday().add(lesson);
                break;
            case WEDNESDAY:
                completeTimetable.getWednesday().add(lesson);
                break;
            case THURSDAY:
                completeTimetable.getThursday().add(lesson);
                break;
            case FRIDAY:
                completeTimetable.getFriday().add(lesson);
                break;
        }
    }

    public static void deleteEmptyLessonFromTop(CompleteTimetable completeTimetable) {

        for(int i = completeTimetable.getMonday().size()-1; i >= 0; i--) {
            if(!completeTimetable.getMonday().get(i).getSubentries().isEmpty()) break;
            completeTimetable.getMonday().remove(i);
        }

        for (int i = completeTimetable.getTuesday().size()-1; i >= 0; i--) {
            if(!completeTimetable.getTuesday().get(i).getSubentries().isEmpty()) break;
            completeTimetable.getTuesday().remove(i);
        }

        for(int i = completeTimetable.getWednesday().size()-1; i >= 0; i--) {
            if(!completeTimetable.getWednesday().get(i).getSubentries().isEmpty()) break;
            completeTimetable.getWednesday().remove(i);
        }

        for(int i = completeTimetable.getThursday().size()-1; i >= 0; i--) {
            if(!completeTimetable.getThursday().get(i).getSubentries().isEmpty()) break;
            completeTimetable.getThursday().remove(i);
        }

        for(int i = completeTimetable.getFriday().size()-1; i >= 0; i--) {
            if(!completeTimetable.getFriday().get(i).getSubentries().isEmpty()) break;
            completeTimetable.getFriday().remove(i);
        }
    }

    public static String extractTitle(Document document) {
        return document.select(".tytulnapis").first().text();
    }

    public static String deleteAllWhitespacesWithLeadingZero(String string) {
        return string.replace(" ", "").replaceAll(RegexQuery.ADD_LEADING_ZERO.toString(), "0$1");
    }

    public static DaysEnum getNextDayOfWeek(DaysEnum currentDay) {
        boolean present = currentDay.next().isPresent();
        return present ? currentDay.next().get() : null;
    }
}