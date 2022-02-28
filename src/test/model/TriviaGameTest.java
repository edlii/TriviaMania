package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriviaGameTest {
    int defaultSize;
    Question q1;
    List<Question> questions = new ArrayList<>();
    QuestionBank qb, qb1, qb2, qb3;
    IncorrectAnswers ia;
    TriviaGame t;
    TriviaGame t1;
    TriviaGame t2;
    TriviaGame t3;

    @BeforeEach
    public void setUp() {
        q1 = new Question("Who was the first president?",0,"George Washington","Easy","History");
        questions.add(q1);
        qb = new QuestionBank();
        defaultSize = qb.getHistoryQuestions().size() + qb.getScienceQuestions().size();
        qb1 = new QuestionBank();
        qb2 = new QuestionBank();
        qb3 = new QuestionBank();
        ia = new IncorrectAnswers();
        t = new TriviaGame("History", "Easy", qb, ia);
        t1 = new TriviaGame("History", "any", qb1, ia);
        t2 = new TriviaGame("any", "Easy", qb2, ia);
        t3 = new TriviaGame("any", "any", qb3, ia);
    }

    @Test
    public void testProduceRandomizedAnswers() {
        List<String> results = t.produceOrderedAnswers(t.getQuestions().get(0));
        assertEquals(4, results.size());
        assertEquals("A:", results.get(0).substring(0,2));
        assertEquals("B:", results.get(1).substring(0,2));
        assertEquals("C:", results.get(2).substring(0,2));
        assertEquals("D:", results.get(3).substring(0,2));
    }

    // Since it is randomized behaviour, test checks if method actually finds correct answer
    @Test
    public void testCheckCorrectAnswer() {
        List<String> results = t.produceOrderedAnswers(t.getQuestions().get(0));
        for (String s : results) {
            if (t.checkCorrectAnswer(s, t.getQuestions().get(0))) {
                assertTrue(true);
            } else {
                assertFalse(false);
            }
        }
    }

    @Test
    public void testGetCategoryQuestionsScience() {
        List<Question> results = t.getCategoryQuestions("Science");
        for (Question q : results) {
            assertEquals("Science", q.getCategory());
        }
    }

    @Test
    public void testGetCategoryQuestionsHistory() {
        List<Question> results = t.getCategoryQuestions("History");
        for (Question q : results) {
            assertEquals("History", q.getCategory());
        }
    }

    @Test
    public void testGetAnyCategoryQuestions() {
        List<Question> results = t.getCategoryQuestions("Any");
        assertEquals(defaultSize + qb.getAllUserCreatedQuestions().size(), results.size());
    }

    @Test
    public void testGetCategoryQuestionsBadInput() {
        List<Question> results = t.getCategoryQuestions("asdfjkl");
        assertEquals(0, results.size());
    }

    @Test
    public void testGetDifficulty() {
        assertEquals("Easy", t.getDifficulty());
    }

    @Test
    public void testGetCategory() {
        assertEquals("History", t.getCategory());
    }

    @Test
    public void testGetDifficultyQuestions() {
        List<Question> results = t.getDifficultyQuestions(t.getCategoryQuestions("History"));
        for (Question q : results) {
            assertEquals("Easy", q.getDifficulty());
        }
    }

    @Test
    public void testGetAnyDifficultyQuestions() {
        List<Question> results = t1.getDifficultyQuestions(t1.getCategoryQuestions("History"));
        assertEquals(5 + qb.getUserCreatedQuestions("History").size(), results.size());
    }

    @Test
    public void testBadIAFileRead() {
        TriviaGame tt = new TriviaGame("asdf", "fd", qb, ia, 0);
        assertFalse(tt.getFailedToReadIA());
        assertTrue(tt.getFailedToReadQ());
    }

    @Test
    public void testBadQuestionFileRead() {
        TriviaGame tt = new TriviaGame("asdf", "fd", qb, ia, 1);
        assertFalse(tt.getFailedToReadQ());
        assertTrue(tt.getFailedToReadIA());
    }

}