package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncorrectAnswersTest {
    IncorrectAnswers emptyIA;
    IncorrectAnswers someIA;
    QuestionBank questionBank;

    @BeforeEach
    public void setUp() {
        emptyIA = new IncorrectAnswers();
        someIA = new IncorrectAnswers();

        List<String> testIncorrectAnswers = new ArrayList<>();
        testIncorrectAnswers.add("1");
        testIncorrectAnswers.add("2");
        testIncorrectAnswers.add("3");
        someIA.addIncorrectAnswers(testIncorrectAnswers);

        questionBank = new QuestionBank();
    }

    @Test
    public void testInitCompleted() {
        assertEquals(10, emptyIA.getIncorrectAnswers().size());
    }

    @Test
    public void testReturnAtIndex() {
        List<String> results = emptyIA.returnAnswersAtIndex(0);
        assertEquals(3, results.size());
        assertEquals("James Corden", results.get(0));
        assertEquals("Kanye West", results.get(1));
        assertEquals("Barack Obama", results.get(2));
    }

    @Test
    public void testAddIncorrectAnswers() {
        List<String> newIncorrectAnswers = new ArrayList<>();
        List<String> newIncorrectAnswers2 = new ArrayList<>();
        newIncorrectAnswers.add("im the first incorrect answer!");
        newIncorrectAnswers.add("im the second incorrect answer!");
        newIncorrectAnswers.add("im the third incorrect answer!");
        newIncorrectAnswers2.add("im the first incorrect answer!");
        newIncorrectAnswers2.add("im the second incorrect answer!");
        newIncorrectAnswers2.add("im the third incorrect answer!");
        emptyIA.addIncorrectAnswers(newIncorrectAnswers);
        assertEquals(11, emptyIA.getIncorrectAnswers().size());
        emptyIA.addIncorrectAnswers(newIncorrectAnswers2);
        assertEquals(12, emptyIA.getIncorrectAnswers().size());
    }

    @Test
    void testToJson() {
        JSONObject json = someIA.toJson();
        Object incorrectAnswers = json.get("Incorrect Answers");
        assertEquals("[{\"incorrectAnswers\":[\"1\",\"2\",\"3\"]}]", incorrectAnswers.toString());
    }


}
