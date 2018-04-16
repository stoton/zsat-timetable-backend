package com.github.stoton.parser;

import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.domain.CssQuery;
import com.github.stoton.domain.TimetableIndexItem;
import com.github.stoton.domain.TimetableType;
import com.github.stoton.tools.AppProperties;
import com.github.stoton.tools.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TimetableParserImpl implements Parser {

    @Override
    public CompleteTimetable parseZsatDocument(String url, String type) throws ParseException, IOException {
        Document document = Jsoup.connect(url).get();

        return ParserFactory.createParser(TimetableType.parseTimetableType(type))
                .parseDocument(document);
    }

    @Override
    public List<TimetableIndexItem> parseDataFromZsatTimetableIndex() throws IOException {

        Document document = Jsoup.connect(AppProperties.ZSAT_TIMETABLE_INDEX_URL).get();

        List<TimetableIndexItem> items = new ArrayList<>();

        Elements elements = document.select(CssQuery.ALL_ELEMENTS_FROM_H4_WITH_UL_DESCENDANT.toString());

        Elements classes = elements.select(CssQuery.CLASSES_ELEMENTS_FROM_PAGE.toString());
        Elements teachers = elements.select(CssQuery.TEACHERS_ELEMENTS_FROM_PAGE.toString());
        Elements classrooms = elements.select(CssQuery.CLASSROOMS_ELEMENTS_FROM_PAGE.toString());

        List<String> urls = new ArrayList<>();

        Elements urlsElements = elements.select(CssQuery.LINKS_IN_A_LIST.toString());

        for (Element e : urlsElements) {
            urls.add(e.attr("href"));
        }

        ListIterator<String> iterator = urls.listIterator();

        extractAndAddToList(items, classes, "STUDENT", iterator);
        extractAndAddToList(items, teachers, "TEACHER", iterator);
        extractAndAddToList(items, classrooms, "CLASSROOM", iterator);

        return items;
    }

    private void extractAndAddToList(List<TimetableIndexItem> list,
                                     Elements elements, String category, ListIterator<String> iterator) {
        for (Element e : elements) {
            String name = Utils.parseName(e.text());

            String link = iterator.next();
            TimetableIndexItem indexItem = new TimetableIndexItem(name, category, link, AppProperties.ZSAT_TIMETABLE_ROOT_URL + link);

            if (category.equals("TEACHER")) {
                String teacherID;

                Pattern pattern = Pattern.compile("\\([a-zśłA-ZŁŚ]+\\)");
                Matcher matcher = pattern.matcher(e.text());
                if (matcher.find()) {
                    teacherID = matcher.group(0);
                    teacherID = teacherID.substring(1, 3);
                    indexItem.setTeacherID(teacherID);
                }
            }

            list.add(indexItem);
        }
    }
}
