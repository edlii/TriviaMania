package model;

import org.json.JSONObject;
import persistence.Writable;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a score having a name and a score number
public class Score implements Writable {
    private String name;
    private String score;

    // Constructor
    // EFFECTS: constructs a score listing with a name and score number
    public Score(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    // EFFECTS: returns string representation of this score
    public String toString() {
        return name + ": " + score;
    }

    // EFFECTS: converts score into a JSON
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("score", score);
        return json;
    }
}
