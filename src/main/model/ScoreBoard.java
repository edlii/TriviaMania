package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a scoreboard having a list of scores
// INVARIANT: scores.size() <= 5
public class ScoreBoard implements Writable {
    private String name;
    private List<Score> scores;
    private final int maxNumHighScores = 5;

    // Constructor
    // EFFECTS: constructs scoreboard with a name and empty list of scores
    public ScoreBoard(String name) {
        this.name = name;
        scores = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    // MODIFIES: this
    // EFFECTS: adds score to this scoreboard
    public void addScore(Score score) {
        scores.add(score);
    }

    // EFFECTS: returns an unmodifiable list of scores in this scoreboard
    public List<Score> getScores() {
        return Collections.unmodifiableList(scores);
    }

    // EFFECTS: returns number of scores in this scoreboard
    public int numScores() {
        return scores.size();
    }

    // MODIFIES: this
    // EFFECTS: checks if current score beats any listed high scores, if is greater or equal to any of the scores,
    //          replace the existing score, otherwise, keep previous scoreboard
    public void newHighScoreOrNot(String name, int score) {
        organizeScoreBoard();
        Score s = new Score(name, Integer.toString(score));
        if (scores.size() == maxNumHighScores) {
            for (int i = 0; i < maxNumHighScores; i++) {
                if (score >= Integer.parseInt(scores.get(i).getScore())) {
                    scores.add(i, s);
                    scores.remove(5);
                    break;
                }
            }
        } else {
            addScore(s);
            organizeScoreBoard();
        }
    }

    // MODIFIES: this
    // EFFECTS: reorganizes the scores in a descending order
    private void organizeScoreBoard() {
        for (int i = 0; i < scores.size(); i++) {
            for (int j = i + 1; j < scores.size(); j++) {
                if (Integer.parseInt(scores.get(i).getScore()) <= Integer.parseInt(scores.get(j).getScore())) {
                    Score temp = scores.get(j);
                    scores.set(j, scores.get(i));
                    scores.set(i, temp);
                }
            }
        }
    }

    // EFFECTS: creates a new JSON object with board name and list of scores
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("score", scoresToJson());
        return json;
    }

    // EFFECTS: returns the scores in this scoreboard as a JSON array
    private JSONArray scoresToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Score s : scores) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }


}
