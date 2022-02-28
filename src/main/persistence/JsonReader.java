package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import model.Question;
import model.Score;
import model.ScoreBoard;
import org.json.*;

// This class references code from this repo
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a reader that reads scorebaord, user questions, and user incorrect answers from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads scoreboard from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ScoreBoard read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseScoreboard(jsonObject);
    }

    // EFFECTS: reads questions from file and returns it;
    // throws IOException if an error occurs reading data from file
    public List<Question> readQuestions() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseUserQuestions(jsonObject);
    }

    // EFFECTS: reads incorrect answers from file and returns it;
    // throws IOException if an error occurs reading data from file
    public List<List<String>> readIncorrectAnswers() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseIncorrectAnswers(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses scoreboard from JSON object and returns it
    private ScoreBoard parseScoreboard(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        ScoreBoard sb = new ScoreBoard(name);
        addScores(sb, jsonObject);
        return sb;
    }

    // MODIFIES: scoreboard
    // EFFECTS: parses scores from JSON object and adds them to scoreboard
    private void addScores(ScoreBoard sb, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("score");
        for (Object json : jsonArray) {
            JSONObject nextScore = (JSONObject) json;
            addScore(sb, nextScore);
        }
    }

    // MODIFIES: scoreboard
    // EFFECTS: parses a score from JSON object and adds it to scoreboard
    private void addScore(ScoreBoard sb, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String score = jsonObject.getString("score");
        Score scoreObject = new Score(name, score);
        sb.addScore(scoreObject);
    }

    // EFFECTS: parses questions from JSON object and returns it
    private List<Question> parseUserQuestions(JSONObject jsonObject) {
        List<Question> listOfQuestions = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("Question");
        for (Object json : jsonArray) {
            JSONObject nextQuestion = (JSONObject) json;
            Question q = getQuestionJson(nextQuestion);
            listOfQuestions.add(q);
        }
        return listOfQuestions;
    }

    // EFFECTS: parses incorrect answers from JSON object and returns it
    private List<List<String>> parseIncorrectAnswers(JSONObject jsonObject) {
        List<List<String>> listOfLos = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("Incorrect Answers");
        for (Object json : jsonArray) {
            JSONObject nextSetOfIa = (JSONObject) json;
            List<String> los = getSetOfIncorrectAnswers(nextSetOfIa);
            listOfLos.add(los);
        }
        return listOfLos;
    }

    // EFFECTS: return each incorrect answer string in the JSON array as a regular list
    private List<String> getSetOfIncorrectAnswers(JSONObject jsonObject) {
        List<String> los = new ArrayList<>();
        JSONArray temp = jsonObject.getJSONArray("incorrectAnswers");
        for (int i = 0; i < temp.length(); i++) {
            los.add(temp.getString(i));
        }
        return los;
    }

    // EFFECTS: returns question in JSON file as a regular question
    private Question getQuestionJson(JSONObject jsonObject) {
        String question = jsonObject.getString("question");
        String correctAnswer = jsonObject.getString("correctAnswer");
        int iaIndex = jsonObject.getInt("incorrectAnswersIndex");
        String difficulty = jsonObject.getString("difficulty");
        String category = jsonObject.getString("category");
        return new Question(question, iaIndex, correctAnswer, difficulty, category);
    }
}
