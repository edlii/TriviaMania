package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionBankTest {
    QuestionBank questionBank;

    @BeforeEach
    public void setUp() {
        questionBank = new QuestionBank();
    }

    @Test
    public void testGetScienceQuestions() {
        List<Question> scienceQuestions = questionBank.getScienceQuestions();
        for (Question q : scienceQuestions) {
            assertEquals("Science", q.getCategory());
        }
    }

    @Test
    public void testGetHistoryQuestions() {
        List<Question> historyQuestions = questionBank.getHistoryQuestions();
        for (Question q : historyQuestions) {
            assertEquals("History", q.getCategory());
        }
    }

    @Test
    public void testAddAndGetCustomQuestions() {
        Question q = new Question("Testing!", 3, "ok!", "Hard", "History");
        Question q2 = new Question("test!", 5, "yes!", "Medium", "Science");
        questionBank.addCustomQuestion(q);
        assertEquals(1, questionBank.getAllUserCreatedQuestions().size());
        questionBank.addCustomQuestion(q2);
        assertEquals(2, questionBank.getAllUserCreatedQuestions().size());
        assertEquals(1, questionBank.getUserCreatedQuestions("Science").size());
        assertEquals(1, questionBank.getUserCreatedQuestions("History").size());
    }

    @Test
    public void testSetAllUserQuestions() {
        List<Question> questions = new ArrayList<>();
        Question question = new Question("asdf", 2, "wrw", "easy", "ete");
        questions.add(question);
        assertEquals(0, questionBank.getAllUserCreatedQuestionsModifiable().size());
        questionBank.setAllUserQuestions(questions);
        assertEquals(1, questionBank.getAllUserCreatedQuestionsModifiable().size());
    }

    @Test
    public void testSimuluateUIRemovingUserQuestion() {
        List<Question> questions = new ArrayList<>();
        List<Question> emptyQuestions = new ArrayList<>();
        Question question = new Question("asdf", 2, "wrw", "easy", "ete");
        questions.add(question);
        assertEquals(0, questionBank.getAllUserCreatedQuestionsModifiable().size());
        questionBank.setAllUserQuestions(questions);
        assertEquals(1, questionBank.getAllUserCreatedQuestionsModifiable().size());
        questionBank.setAllUserQuestions(emptyQuestions);
        assertEquals(0, questionBank.getAllUserCreatedQuestionsModifiable().size());
    }

    @Test
    public void testQuestionToJson() {
        List<Question> questions = new ArrayList<>();
        Question question = new Question("asdf", 2, "wrw", "easy", "History");
        Question question2 = new Question("asdf", 2, "wrw", "easy", "Science");
        questions.add(question);
        questions.add(question2);
        questionBank.setAllUserQuestions(questions);
        JSONObject json = questionBank.toJson();
        JSONObject json2 = question.toJson();
        JSONObject json3 = question2.toJson();
        assertEquals("[" + json2.toString() + "," + json3.toString() + "]", json.get("Question").toString());
    }
}
