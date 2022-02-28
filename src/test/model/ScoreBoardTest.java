package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreBoardTest {
    ScoreBoard emptySb;
    ScoreBoard generalSb;
    ScoreBoard maxSb;
    Score s1 = new Score("James", "22");
    Score s2 = new Score("Toby", "11");
    Score s3 = new Score("Brandon", "88");
    Score s4 = new Score("Vincent", "54");
    Score s5 = new Score("Harry", "43");

    @BeforeEach
    void setUp() {
        emptySb = new ScoreBoard("High Scores");
        generalSb = new ScoreBoard("High Scores");
        maxSb = new ScoreBoard("High Scores");

        generalSb.addScore(s2);
        generalSb.addScore(s4);

        maxSb.addScore(s1);
        maxSb.addScore(s2);
        maxSb.addScore(s3);
        maxSb.addScore(s4);
        maxSb.addScore(s5);
    }

    @Test
    void testEmptyNewHighScoreOrNot() {
        assertEquals("High Scores", emptySb.getName());
        emptySb.newHighScoreOrNot("Doug", 99);
        assertEquals(1, emptySb.numScores());
    }

    @Test
    void testGeneralNewHighScoreOrNot() {
        assertEquals("High Scores", generalSb.getName());
        generalSb.newHighScoreOrNot("Doug", 99);
        assertEquals(3, generalSb.numScores());
        generalSb.newHighScoreOrNot("Bobby", 66);
        assertEquals(4, generalSb.numScores());
    }

    @Test
    void testMaxNewHighScoreOrNot() {
        assertEquals("High Scores", maxSb.getName());
        maxSb.newHighScoreOrNot("Doug", 99);
        assertEquals("Doug", maxSb.getScores().get(0).getName());
        assertEquals(5, maxSb.numScores());
        maxSb.newHighScoreOrNot("Bobby", 0);
        assertEquals("Doug", maxSb.getScores().get(0).getName());
        assertEquals("Brandon", maxSb.getScores().get(1).getName());
        assertEquals("Vincent", maxSb.getScores().get(2).getName());
        assertEquals("Harry", maxSb.getScores().get(3).getName());
        assertEquals("James", maxSb.getScores().get(4).getName());
        assertEquals(5, maxSb.numScores());
    }

    @Test
    void testEmptyToJson() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyScoreboard.json");
            writer.open();
            writer.write(emptySb);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterEmptyScoreboard.json");

            emptySb = reader.read();
            assertEquals("High Scores", emptySb.getName());
            assertEquals(0, emptySb.numScores());
        } catch (IOException e) {
            fail("There was an error creating the Json object.");
        }
    }

    @Test
    void testGeneralToJson() {
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
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
