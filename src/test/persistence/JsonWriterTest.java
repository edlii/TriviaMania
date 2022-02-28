package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            ScoreBoard sb = new ScoreBoard("High Scores");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyScoreboard() {
        try {
            ScoreBoard sb = new ScoreBoard("High Scores");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyScoreboard.json");
            writer.open();
            writer.write(sb);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyScoreboard.json");
            sb = reader.read();
            assertEquals("High Scores", sb.getName());
            assertEquals(0, sb.numScores());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralScoreboard() {
        try {
            ScoreBoard sb = new ScoreBoard("High Scores");
            sb.addScore(new Score("doug", "66"));
            sb.addScore(new Score("mr.krabs","99"));
            sb.addScore(new Score("big rabbit","99"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralScoreboard.json");
            writer.open();
            writer.write(sb);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralScoreboard.json");
            sb = reader.read();
            assertEquals("High Scores", sb.getName());
            List<Score> scores = sb.getScores();
            assertEquals(3, scores.size());
            checkScore("doug", 66, scores.get(0));
            checkScore("mr.krabs", 99, scores.get(1));
            checkScore("big rabbit", 99, scores.get(2));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterOverFiveEntriesScoreboard() {
        try {
            ScoreBoard sb = new ScoreBoard("High Scores");
            sb.addScore(new Score("doug", "66"));
            sb.addScore(new Score("mr.krabs","99"));
            sb.addScore(new Score("big rabbit","99"));
            sb.addScore(new Score("dababy","21"));
            sb.addScore(new Score("mario","10"));
            JsonWriter writer = new JsonWriter("./data/testWriterMaxEntriesScoreboard.json");
            writer.open();
            writer.write(sb);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterMaxEntriesScoreboard.json");
            sb = reader.read();
            assertEquals("High Scores", sb.getName());
            List<Score> scores = sb.getScores();
            assertEquals(5, scores.size());
            checkScore("doug", 66, scores.get(0));
            checkScore("mr.krabs", 99, scores.get(1));
            checkScore("big rabbit", 99, scores.get(2));
            checkScore("dababy", 21, scores.get(3));
            checkScore("mario", 10, scores.get(4));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterQuestionBankEmpty() {
        try {
            QuestionBank qb = new QuestionBank();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyUserQuestions.json");
            writer.open();
            writer.write(qb);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyUserQuestions.json");
            List<Question> questions = reader.readQuestions();
            assertEquals(0, questions.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterQuestionBankSome() {
        try {
            QuestionBank qb = new QuestionBank();
            Question q = new Question("Testing!", 3, "ok!", "Hard", "History");
            Question q2 = new Question("test!", 5, "yes!", "Medium", "Science");
            qb.addCustomQuestion(q);
            qb.addCustomQuestion(q2);
            JsonWriter writer = new JsonWriter("./data/testWriterSomeUserQuestions.json");
            writer.open();
            writer.write(qb);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterSomeUserQuestions.json");
            List<Question> questions = reader.readQuestions();
            assertEquals(2, questions.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterIncorrectAnswersEmpty() {
        try {
            IncorrectAnswers ia = new IncorrectAnswers();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyUserIncorrectAnswers.json");
            writer.open();
            writer.write(ia);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyUserIncorrectAnswers.json");
            List<List<String>> answers = reader.readIncorrectAnswers();
            assertEquals(0, answers.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterIncorrectAnswersSome() {
        try {
            IncorrectAnswers ia = new IncorrectAnswers();
            List<String> newIncorrectAnswers = new ArrayList<>();
            List<String> newIncorrectAnswers2 = new ArrayList<>();
            newIncorrectAnswers.add("im the first incorrect answer!");
            newIncorrectAnswers.add("im the second incorrect answer!");
            newIncorrectAnswers.add("im the third incorrect answer!");
            newIncorrectAnswers2.add("im the first incorrect answer!");
            newIncorrectAnswers2.add("im the second incorrect answer!");
            newIncorrectAnswers2.add("im the third incorrect answer!");
            ia.addIncorrectAnswers(newIncorrectAnswers);
            ia.addIncorrectAnswers(newIncorrectAnswers2);
            JsonWriter writer = new JsonWriter("./data/testWriterSomeUserIncorrectAnswers.json");
            writer.open();
            writer.write(ia);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterSomeUserIncorrectAnswers.json");
            List<List<String>> answers = reader.readIncorrectAnswers();
            assertEquals(2, answers.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
