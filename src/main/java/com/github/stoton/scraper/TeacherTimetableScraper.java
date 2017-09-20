package com.github.stoton.scraper;

import com.github.stoton.domain.*;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.tools.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TeacherTimetableScraper implements TimetableScraper {

    @Override
    public DayContener parseDocument(Document document, TimetableIndexItemRepository timetableIndexItemRepository) throws ParseException {
        Elements elements = document.select(CssQuery.HTML_TABLE_CLASS.toString());

        List<Lesson> monday = new ArrayList<>();
        List<Lesson> tuesday = new ArrayList<>();
        List<Lesson> wednesday = new ArrayList<>();
        List<Lesson> thursday = new ArrayList<>();
        List<Lesson> friday = new ArrayList<>();

        for (int x = 1; x < elements.select(CssQuery.TR_ELEMENT.toString()).size(); x++) {

            Element element = elements.select(CssQuery.TR_ELEMENT.toString()).get(x);

            Elements td = element.select(CssQuery.L_CLASS.toString());

            String nr = element.select(CssQuery.NR_CLASS.toString()).text();
            String timePhase = element.select(CssQuery.HOUR_CLASS.toString()).text();

            timePhase = timePhase.replace(" ", "").replaceAll(RegexQuery.ADD_LEADING_ZERO.toString(), "0$1");


            for (int c = 0; c < td.size(); c++) {
                List<String> secondaryText;
                Lesson lesson = new Lesson();
                lesson.setLessonNumber(nr);
                lesson.setTimePhase(timePhase);

                secondaryText = Utils.extractElementsByRegex(td.text(), RegexQuery.EXTRACT_STRING_BY_CLASS.toString());

                Elements p = td.get(c).select(CssQuery.P_CLASS.toString());
                Elements o = td.get(c).select(CssQuery.O_CLASS.toString());
                Elements s = td.get(c).select(CssQuery.S_CLASS.toString());

                ListIterator<Element> addonItemsIterator = s.listIterator();
                int j = 0;
                for (int i = 0; i < p.size(); i++) {
                    String primary;
                    String second;

                    primary = p.get(i).text();
                    second = o.get(i).text();
                    j++;


                    String addon = addonItemsIterator.next().text();

                    Subentry subentry = new Subentry();

                    String originalSecondary = "";
                    for (String aSecondaryText : secondaryText) {
                        originalSecondary = Utils.filter(second, aSecondaryText);
                        if (!originalSecondary.equals("")) {
                            secondaryText.remove(aSecondaryText);
                            break;
                        }
                    }

                    if(originalSecondary.equals(""))
                        subentry.setSecondaryText(Utils.addSpaceToIndex(second, 0));
                    else
                        subentry.setSecondaryText(Utils.addSpaceToIndex(originalSecondary, 0));

                    subentry.setPrimaryText(primary);

                    subentry.setAddon(addon);
                    lesson.getSubentries().add(subentry);
                }

                int choice = c % 5;
                Utils.addLessonToDay(monday, tuesday, wednesday, thursday, friday, lesson, choice);
            }
        }

        return new DayContener.DayContenerBuilder()
                .monday(monday)
                .tuesday(tuesday)
                .wednesday(wednesday)
                .thursday(thursday)
                .friday(friday)
                .build();
    }
}
