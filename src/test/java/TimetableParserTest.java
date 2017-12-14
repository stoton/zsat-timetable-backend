import com.github.stoton.domain.CompleteTimetable;
import com.github.stoton.domain.Lesson;
import com.github.stoton.domain.Subentry;
import com.github.stoton.parser.TeacherTimetableParser;
import com.github.stoton.parser.TimetableParser;
import com.github.stoton.repository.TimetableIndexItemRepository;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TimetableParserTest {

    private TimetableParser timetableParser;

    @Mock
    private TimetableIndexItemRepository timetableIndexItemRepository;

    @Before
    public void init() {
        timetableParser = new TeacherTimetableParser(timetableIndexItemRepository);
    }

    @Test
    public void teacherParserTestWhenDocumentIsCorrect() throws IOException, ParseException {

        List<Subentry> mondaySubentry = new ArrayList<>();

        mondaySubentry.add(new Subentry("pra.w ob.at", "2 Tia-2/2", "216"));

        Lesson mondayLesson = Lesson
                .builder()
                .lessonNumber("7")
                .timePhase("13:15-14:00")
                .subentries(mondaySubentry)
                .build();

        CompleteTimetable expected = CompleteTimetable
                .builder()
                .monday(new ArrayList<>(Collections.singletonList(mondayLesson)))
                .tuesday(new ArrayList<>())
                .wednesday(new ArrayList<>())
                .thursday(new ArrayList<>())
                .friday(new ArrayList<>())
                .build();

        InputStream file = this.getClass().getResourceAsStream("correctTeacherTimetable.html");

        String html = CharStreams.toString(new InputStreamReader(
                file, Charsets.UTF_8));

        Document document = Jsoup.parse(html);

        CompleteTimetable actual = timetableParser.parseDocument(document);

        assertEquals(expected, actual);

    }
}
