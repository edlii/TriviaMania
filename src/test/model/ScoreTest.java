package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreTest {
    Score s1;
    Score s2;
    Score s3;

    @BeforeEach
    void setUp() {
        s1 = new Score("James", "22");
        s2 = new Score("Toby", "11");
        s3 = new Score("Brandon", "88");
    }

    @Test
    void testGetName() {
        assertEquals("James", s1.getName());
        assertEquals("Toby", s2.getName());
        assertEquals("Brandon", s3.getName());
    }

    @Test
    void testGetScore() {
        assertEquals("88", s3.getScore());
        assertEquals("11", s2.getScore());
        assertEquals("22", s1.getScore());
    }

    @Test
    void testToString() {
        assertEquals("James: 22", s1.toString());
        assertEquals("Brandon: 88", s3.toString());
        assertEquals("Toby: 11", s2.toString());
    }

    @Test
    void testToJson() {
        JSONObject json = s1.toJson();
        JSONObject json2 = s2.toJson();
        JSONObject json3 = s3.toJson();

        assertEquals("James", json.get("name"));
        assertEquals("Toby", json2.get("name"));
        assertEquals("Brandon", json3.get("name"));

        assertEquals("22", json.get("score"));
        assertEquals("11", json2.get("score"));
        assertEquals("88", json3.get("score"));
    }
}
