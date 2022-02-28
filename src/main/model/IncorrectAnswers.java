package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;
import persistence.Writable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// All the incorrect answers for the questions
// "incorrect answers bank"
public class IncorrectAnswers implements Writable {
    private List<List<String>> incorrectAnswers;

    // Constructor
    public IncorrectAnswers() {
        incorrectAnswers = new ArrayList<>();
        initHistory();
        initScience();
    }

    public List<List<String>> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    // MODIFIES: this
    // EFFECTS: initializes all history incorrect answers. Adds them to the list
    public void initHistory() {
        List<String> answerList = new ArrayList<>();
        answerList.add("James Corden");
        answerList.add("Kanye West");
        answerList.add("Barack Obama");
        answerList.add("The American Civil War");
        answerList.add("The Vietnam War");
        answerList.add("World War 1");
        answerList.add("Gregor Kiczales");
        answerList.add("Ada Lovelace");
        answerList.add("Paul Carter");
        answerList.add("Facebook");
        answerList.add("Skype");
        answerList.add("WeChat");
        answerList.add("1923");
        answerList.add("1865");
        answerList.add("1000 BCE");
        for (int i = 0; i < answerList.size() / 3; i++) { // Adds to list in threes (grouped answers)
            List<String> tempList = new ArrayList<>();
            for (int j = i * 3; j < (i * 3) + 3; j++) {
                tempList.add(answerList.get(j));
            }
            incorrectAnswers.add(tempList);
        }
    }


    // MODIFIES: this
    // EFFECTS: initializes all science incorrect answers. Adds them to the list
    public void initScience() {
        List<String> answerList = new ArrayList<>();
        answerList.add("I");
        answerList.add("Ag");
        answerList.add("F");
        answerList.add("Jupiter");
        answerList.add("Mars");
        answerList.add("Venus");
        answerList.add("O-");
        answerList.add("AB+");
        answerList.add("A-");
        answerList.add("200°C");
        answerList.add("98.6°C");
        answerList.add("101.3°C");
        answerList.add("3 days");
        answerList.add("47 days");
        answerList.add("200 days");
        for (int i = 0; i < answerList.size() / 3; i++) { // Adds to list in threes (grouped answers)
            List<String> tempList = new ArrayList<>();
            for (int j = i * 3; j < (i * 3) + 3; j++) {
                tempList.add(answerList.get(j));
            }
            incorrectAnswers.add(tempList);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds incorrect answers to the list of incorrect answers
    public void addIncorrectAnswers(List<String> ia) {
        incorrectAnswers.add(ia);
        EventLog.getInstance().logEvent(new Event("Added its incorrect answers to the incorrect answers bank."));
    }

    // REQUIRES: index >= 0
    // EFFECTS: gets answers at given index
    public List<String> returnAnswersAtIndex(int index) {
        return incorrectAnswers.get(index);
    }

    // EFFECTS: returns all the questions starting after index 9 (end of defaults)
    //          does quick check of index to ensure no out of bounds exception
    public List<List<String>> getUserCreatedIncorrectAnswers() {
        List<List<String>> userIa = new ArrayList<>();
        for (int i = 9; i < incorrectAnswers.size(); i++) {
            if (i > 9) {
                userIa.add(incorrectAnswers.get(i));
            }
        }
        return Collections.unmodifiableList(userIa);
    }

    // MODIFIES: JSON
    // EFFECTS: returns the user created incorrect answers as a JSON array
    private JSONArray incorrectAnswersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (List<String> los  : getUserCreatedIncorrectAnswers()) {
            jsonArray.put(toJson(los));
        }

        return jsonArray;
    }

    // EFFECTS: turns given list of string into a JSONObject
    public JSONObject toJson(List<String> los) {
        JSONObject json = new JSONObject();
        json.put("incorrectAnswers", los);
        return json;
    }

    // EFFECTS: creates a JSON object with incorrect answers
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Incorrect Answers", incorrectAnswersToJson());
        return json;
    }

}
