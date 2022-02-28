package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionTest {
    Question q1;
    Question q2;
    IncorrectAnswers incorrectAnswersBank;

    @BeforeEach
    public void setUp() {
        q1 = new Question("Who was the first president?",0,"George Washington","Easy","History");
        q2 = new Question("What is the chemical formula for iron?", 5, "Fe", "Easy", "Science");
        incorrectAnswersBank = new IncorrectAnswers();
    }

    @Test
    public void testGetIncorrectAnswers() {
        List<String> results = q1.getIncorrectAnswers(incorrectAnswersBank);
        assertEquals(3, results.size());
        assertEquals("James Corden", results.get(0));
        assertEquals("Kanye West", results.get(1));
        assertEquals("Barack Obama", results.get(2));

        results = q2.getIncorrectAnswers(incorrectAnswersBank);
        assertEquals(3, results.size());
        assertEquals("I", results.get(0));
        assertEquals("Ag", results.get(1));
        assertEquals("F", results.get(2));
    }

    @Test
    public void testGetQuestion() {
        assertEquals("Who was the first president?", q1.getQuestion());
    }

    @Test
    public void testGetCorrectAnswer() {
        assertEquals("George Washington", q1.getCorrectAnswer());
    }

    @Test
    public void testGetCategory() {
        assertEquals("Science", q2.getCategory());
    }

    @Test
    public void testGetDifficulty() {
        assertEquals("Easy", q2.getDifficulty());
    }

    @Test
    public void testGetAndSetIncorrectAnswersIndex() {
        assertEquals(0, q1.getIncorrectAnswersIndex());
        q1.setIncorrectAnswersIndex(343);
        assertEquals(343, q1.getIncorrectAnswersIndex());
    }

    @Test
    public void testToJson() {
        JSONObject json = q1.toJson();
        assertEquals("Who was the first president?", json.get("question"));
        assertEquals(0, json.get("incorrectAnswersIndex"));
        assertEquals("George Washington", json.get("correctAnswer"));
        assertEquals("Easy", json.get("difficulty"));
        assertEquals("History", json.get("category"));
    }
}
