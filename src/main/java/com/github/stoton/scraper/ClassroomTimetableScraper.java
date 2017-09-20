package com.github.stoton.scraper;

import com.github.stoton.domain.*;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.tools.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Component
public class ClassroomTimetableScraper implements TimetableScraper {
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
                Elements n = td.get(c).select(CssQuery.N_CLASS.toString());
                Elements o = td.get(c).select(CssQuery.O_CLASS.toString());

                ListIterator<Element> addonItemsIterator = o.listIterator();

                for (int i = 0; i < p.size(); i++) {

                    String addon = addonItemsIterator.next().text();

                    Subentry subentry = new Subentry();

                    subentry.setPrimaryText(p.get(i).text());

                    TimetableIndexItem timetableIndexItem = timetableIndexItemRepository.findFirstByTeacherID(n.get(i).text());

                    if(timetableIndexItem != null) {
                        subentry.setSecondaryText(timetableIndexItem.getName());
                    }

                    String secondary = "";
                    for (String aSecondaryText : secondaryText) {
                        secondary = Utils.filter(addon, aSecondaryText);
                        if (!secondary.equals("")) {
                            secondaryText.remove(aSecondaryText);
                            break;
                        }
                    }

                    if(secondary.equals(""))
                        subentry.setAddon(Utils.addSpaceToIndex(addon, 0));
                    else
                        subentry.setAddon(Utils.addSpaceToIndex(secondary, 0));
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
