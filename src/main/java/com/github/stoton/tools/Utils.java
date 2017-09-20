package com.github.stoton.tools;

import com.github.stoton.domain.Lesson;

import java.util.ArrayList;
import java.util.List;
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

    public static void addLessonToDay(List<Lesson> monday, List<Lesson> tuesday, List<Lesson> wednesday, List<Lesson> thursday, List<Lesson> friday, Lesson lesson, int choice) {
        switch (choice) {
            case 0:
                monday.add(lesson);
                break;
            case 1:
                tuesday.add(lesson);
                break;
            case 2:
                wednesday.add(lesson);
                break;
            case 3:
                thursday.add(lesson);
                break;
            case 4:
                friday.add(lesson);
                break;
        }
    }
}