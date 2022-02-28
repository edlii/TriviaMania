package model;

import persistence.JsonReader;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// The actual trivia game functionality; the class that does most of the searching and organizing work
public class TriviaGame {
    private static final String JSON_STORE_Q = "./data/userQuestions.json";
    private static final String JSON_STORE_IA = "./data/incorrectAnswers.json";
    private String category;
    private String difficulty;
    private Boolean failedToReadQ;
    private Boolean failedToReadIA;
    private List<Question> questions;
    private QuestionBank questionBank;
    private IncorrectAnswers incorrectAnswersBank;
    private JsonReader jsonReaderIA;
    private JsonReader jsonReaderQ;

    // Constructor
    public TriviaGame(String category, String difficulty, QuestionBank questionBank, IncorrectAnswers iaBank) {
        this.category = category;
        this.difficulty = difficulty;
        this.questionBank = questionBank;
        jsonReaderIA = new JsonReader(JSON_STORE_IA);
        jsonReaderQ = new JsonReader(JSON_STORE_Q);
        incorrectAnswersBank = iaBank;
        loadUserQuestions();
        loadUserIncorrectAnswers();
        questions = getDifficultyQuestions(getCategoryQuestions(category));
    }

    // Constructor for testing purposes
    public TriviaGame(String category, String difficulty, QuestionBank questionBank, IncorrectAnswers iaBank, int i) {
        if (i == 0) {
            this.category = category;
            this.difficulty = difficulty;
            this.questionBank = questionBank;
            jsonReaderIA = new JsonReader(JSON_STORE_IA);
            failedToReadIA = false;
            jsonReaderQ = new JsonReader("bad dest");
            incorrectAnswersBank = iaBank;
            loadUserQuestions();
            loadUserIncorrectAnswers();
            questions = getDifficultyQuestions(getCategoryQuestions(category));
        } else {
            this.category = category;
            this.difficulty = difficulty;
            this.questionBank = questionBank;
            jsonReaderIA = new JsonReader("bad dest");
            jsonReaderQ = new JsonReader(JSON_STORE_Q);
            failedToReadQ = false;
            incorrectAnswersBank = iaBank;
            loadUserQuestions();
            loadUserIncorrectAnswers();
            questions = getDifficultyQuestions(getCategoryQuestions(category));
        }
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Boolean getFailedToReadQ() {
        return failedToReadQ;
    }

    public Boolean getFailedToReadIA() {
        return failedToReadIA;
    }

    // REQUIRES: category string is one of the given categories
    // MODIFIES: this
    // EFFECTS: returns the questions of that given category
    public List<Question> getCategoryQuestions(String category) {
        String lowerCaseCategory = category.toLowerCase();
        List<Question> categoryQuestions = new ArrayList<>();
        if (lowerCaseCategory.equals("history")) {
            categoryQuestions.addAll(questionBank.getHistoryQuestions());
            categoryQuestions.addAll(questionBank.getUserCreatedQuestions("History"));
        } else if (lowerCaseCategory.equals("science")) {
            categoryQuestions.addAll(questionBank.getScienceQuestions());
            categoryQuestions.addAll(questionBank.getUserCreatedQuestions("Science"));
        } else if (lowerCaseCategory.equals("any")) {
            categoryQuestions.addAll(questionBank.getHistoryQuestions());
            categoryQuestions.addAll(questionBank.getScienceQuestions());
            categoryQuestions.addAll(questionBank.getUserCreatedQuestions("History"));
            categoryQuestions.addAll(questionBank.getUserCreatedQuestions("Science"));
        }
        Collections.shuffle(categoryQuestions);
        return categoryQuestions;
    }

    // REQUIRES: difficulty string is one of the given categories
    // MODIFIES: this
    // EFFECTS: returns the questions of that given difficulty
    public List<Question> getDifficultyQuestions(List<Question> categoryQuestions) {
        List<Question> difficultyQuestions = new ArrayList<>();
        List<Question> anyDifficultyQuestions = new ArrayList<>();
        for (Question q : categoryQuestions) {
            String lowerCaseDifficulty = q.getDifficulty().toLowerCase();
            if (lowerCaseDifficulty.equals(difficulty.toLowerCase())) {
                difficultyQuestions.add(q);
            }
            anyDifficultyQuestions.add(q);
        }
        if (!difficulty.equalsIgnoreCase("any")) {
            return difficultyQuestions;
        } else {
            return anyDifficultyQuestions;
        }
    }

    // EFFECTS: copies all given answers and returns them in labelled, randomized order
    public List<String> produceOrderedAnswers(Question question) {
        List<String> allAnswers = new ArrayList<>();
        List<String> randomizedLabelledAnswers = new ArrayList<>();
        allAnswers.add(question.getCorrectAnswer());
        allAnswers.addAll(question.getIncorrectAnswers(incorrectAnswersBank));

        Collections.shuffle(allAnswers);
        randomizedLabelledAnswers.add("A: " + allAnswers.get(0));
        randomizedLabelledAnswers.add("B: " + allAnswers.get(1));
        randomizedLabelledAnswers.add("C: " + allAnswers.get(2));
        randomizedLabelledAnswers.add("D: " + allAnswers.get(3));

        return randomizedLabelledAnswers;
    }

    // EFFECTS: checks if the picked choice is the same as the correct answer
    public Boolean checkCorrectAnswer(String s, Question q) {
        String questionAnswer = q.getCorrectAnswer();
        return s.equals(questionAnswer);
    }

    // MODIFIES: questionBank
    // EFFECTS: loads the user created questions from JSON file and adds it to this question bank
    public void loadUserQuestions() {
        try {
            questionBank.setAllUserQuestions(jsonReaderQ.readQuestions());
        } catch (IOException e) {
            failedToReadQ = true;
        }
    }

    // MODIFIES: incorrectAnswersBank
    // EFFECTS: gets all the user generated incorrect answers from JSON and adds it to the bank
    public void loadUserIncorrectAnswers() {
        try {
            List<List<String>> temp = jsonReaderIA.readIncorrectAnswers();
            incorrectAnswersBank.getIncorrectAnswers().addAll(temp);
        } catch (IOException e) {
            failedToReadIA = true;
        }
    }


}
