package persistence;

import model.Score;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkScore(String name, int score, Score scoreObject) {
        assertEquals(name, scoreObject.getName());
        assertEquals(score, Integer.parseInt(scoreObject.getScore()));
    }
}
