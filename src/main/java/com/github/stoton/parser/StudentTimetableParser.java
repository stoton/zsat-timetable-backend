package com.github.stoton.parser;

import com.github.stoton.domain.*;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.tools.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class StudentTimetableParser implements TimetableParser {

    @Override
    public DayContener parseDocument(Document document, TimetableIndexItemRepository timetableIndexItemRepository) throws ParseException, IOException {
        Elements elements = document.select(CssQuery.HTML_TABLE_CLASS.toString());

        List<Lesson> monday = new ArrayList<>();
        List<Lesson> tuesday = new ArrayList<>();
        List<Lesson> wednesday = new ArrayList<>();
        List<Lesson> thursday = new ArrayList<>();
        List<Lesson> friday = new ArrayList<>();

        for (int x = 1; x < elements.select(CssQuery.TR_ELEMENT.toString()).size(); x++) {

            Element element = elements.select(CssQuery.TR_ELEMENT.toString()).get(x);

            Elements td = element.select(CssQuery.L_CLASS.toString());


            Elements sc = td.select("a[href^=s]");
            List<String> hyperLinks = new ArrayList<>();
            List<String> nameLinks = new ArrayList<>();

            for (Element aSc : sc) {
                hyperLinks.add(aSc.attr("href"));
                nameLinks.add(aSc.text());
            }

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
                Elements s = td.get(c).select(CssQuery.S_CLASS.toString());

                ListIterator<Element> addonItemsIterator = s.listIterator();
                int j = 0;
                for (int i = 0; i < p.size(); i++) {

                    String primary;
                    String second;
                    if(!Utils.isOverRange(i+1, p.size()) && p.get(i+1).text().startsWith("#")) {
                        primary = p.get(i).text();
                        i++;
                        second = p.get(i).text();
                    } else {
                            TimetableIndexItem timetableIndexItem = timetableIndexItemRepository.findFirstByTeacherID(n.get(j).text());
                            primary = p.get(i).text();
                            second = timetableIndexItem.getName();
                            j++;
                    }

                    String addon = addonItemsIterator.next().text();

                    Subentry subentry = new Subentry();

                    String originalPrimary = "";
                    for (String aSecondaryText : secondaryText) {
                        originalPrimary = Utils.filter(primary, aSecondaryText);
                        if (!originalPrimary.equals("")) {
                            secondaryText.remove(aSecondaryText);
                            break;
                        }
                    }

                    if(originalPrimary.equals(""))
                        subentry.setPrimaryText(primary);
                    else
                        subentry.setPrimaryText(originalPrimary);

                    subentry.setSecondaryText(second);

                    for(int cx = 0; cx < nameLinks.size(); cx++) {
                        if(addon.contains(nameLinks.get(cx))) {
                            Document doc = Jsoup.connect("http://szkola.zsat.linuxpl.eu/planlekcji/plany/"  + hyperLinks.get(cx)).get();
                            addon = Utils.parseName(Utils.extractTitle(doc));
                            break;
                        }

                    }

                    subentry.setAddon(addon);
                    lesson.getSubentries().add(subentry);
                }

                int choice = c % 5;
                Utils.addLessonToDay(monday, tuesday, wednesday, thursday, friday, lesson, choice);
            }
        }

        Utils.deleteEmptyLessonFromTop(monday, tuesday, wednesday, thursday, friday);

        return new DayContener.DayContenerBuilder()
                .monday(monday)
                .tuesday(tuesday)
                .wednesday(wednesday)
                .thursday(thursday)
                .friday(friday)
                .build();
    }
}
