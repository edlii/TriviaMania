package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.List;

// Represents each question in the trivia game with appropriate categorizing fields
public class Question implements Writable {
    private String question;
    private int incorrectAnswersIndex;
    private String correctAnswer;
    private String difficulty;
    private String category;

    // Constructor
    public Question(String question, int badAnswersIndex, String goodAnswer, String difficulty, String category) {
        this.question = question;
        incorrectAnswersIndex = badAnswersIndex;
        correctAnswer = goodAnswer;
        this.difficulty = difficulty;
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getIncorrectAnswersIndex() {
        return incorrectAnswersIndex;
    }

    public void setIncorrectAnswersIndex(int index) {
        this.incorrectAnswersIndex = index;
    }

    // REQUIRES: question index has to be kept in line with incorrect answer position
    // EFFECTS: returns incorrect answers at the specified index
    public List<String> getIncorrectAnswers(IncorrectAnswers ia) {
        return ia.returnAnswersAtIndex(incorrectAnswersIndex);
    }

    // EFFECTS: converts question to JSON
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("question", getQuestion());
        json.put("incorrectAnswersIndex", getIncorrectAnswersIndex());
        json.put("correctAnswer", getCorrectAnswer());
        json.put("difficulty", getDifficulty());
        json.put("category", getCategory());
        return json;
    }
}
