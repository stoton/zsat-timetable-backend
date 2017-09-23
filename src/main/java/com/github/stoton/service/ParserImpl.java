package com.github.stoton.service;

import com.github.stoton.domain.DayContener;
import com.github.stoton.domain.TimetableIndexItem;
import com.github.stoton.domain.TimetableType;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.github.stoton.domain.CssQuery;
import com.github.stoton.parser.ParserFactory;
import com.github.stoton.tools.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Service
public class ParserImpl implements Parser {

    @Autowired
    private TimetableIndexItemRepository timetableIndexItemRepository;

    @Override
    public DayContener parseDataFromZsat(String url, String type)  {

        Document document = null;

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return ParserFactory.createParser(TimetableType.parseTimetableType(type))
                    .parseDocument(document, timetableIndexItemRepository);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<TimetableIndexItem> parseDataFromZsatTimetableIndex() throws IOException {
        String INDEX_URL = "http://szkola.zsat.linuxpl.eu/planlekcji/lista.html";

        Document document = Jsoup.connect(INDEX_URL).get();

        List<TimetableIndexItem> items = new ArrayList<>();

        Elements elements = document.select(CssQuery.ALL_ELEMENTS_FROM_H4.toString());

        Elements classes = elements.select(CssQuery.CLASSES_ELEMENTS_FROM_PAGE.toString());
        Elements teachers = elements.select(CssQuery.TEACHERS_ELEMENTS_FROM_PAGE.toString());
        Elements classrooms = elements.select(CssQuery.CLASSROOMS_ELEMENTS_FROM_PAGE.toString());

        List<String> urls = new ArrayList<>();

        Elements urlsElements = elements.select(CssQuery.URLS.toString());

        for(Element e : urlsElements) {
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
        for(Element e : elements) {
            String name = Utils.parseName(e.text());

            TimetableIndexItem indexItem = new TimetableIndexItem(name, category, iterator.next());

            if(category.equals("TEACHER")) {
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
