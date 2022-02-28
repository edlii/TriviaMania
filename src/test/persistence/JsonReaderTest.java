package persistence;

import model.Question;
import model.Score;
import model.ScoreBoard;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/nonExistentFile.json");
        try {
            ScoreBoard sb = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }


    @Test
    void testReaderEmptyScoreboard() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyScoreboard.json");
        try {
            ScoreBoard sb = reader.read();
            assertEquals("High Scores", sb.getName());
            assertEquals(0, sb.numScores());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralScoreboard() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralScoreboard.json");
        try {
            ScoreBoard sb = reader.read();
            assertEquals("High Scores", sb.getName());
            List<Score> scores = sb.getScores();
            assertEquals(2, scores.size());
            checkScore("me", 43, scores.get(0));
            checkScore("test", 2, scores.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderMaxEntriesScoreboard() {
        JsonReader reader = new JsonReader("./data/testReaderMaxEntriesScoreboard.json");
        try {
            ScoreBoard sb = reader.read();
            assertEquals("High Scores", sb.getName());
            List<Score> scores = sb.getScores();
            assertEquals(5, scores.size());
            checkScore("doug", 66, scores.get(0));
            checkScore("mr.krabs", 99, scores.get(1));
            checkScore("big rabbit", 99, scores.get(2));
            checkScore("dababy", 21, scores.get(3));
            checkScore("mario", 10, scores.get(4));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderNonExistentQuestionBankFiles() {
        JsonReader reader = new JsonReader("./data/nonExistentFile.json");
        try {
            List<Question> questions = reader.readQuestions();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderNonExistentIncorrectAnswerFiles() {
        JsonReader reader = new JsonReader("./data/nonExistentFile.json");
        try {
            List<List<String>> answers = reader.readIncorrectAnswers();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }
}
