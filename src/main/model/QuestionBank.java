package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// The question bank that stores all the questions for the game
public class QuestionBank implements Writable {
    private List<List<Question>> allQuestions;

    // Constructor
    public QuestionBank() {
        allQuestions = new ArrayList<>();
        allQuestions.add(initDefaultHistoryQuestions());
        allQuestions.add(initDefaultScienceQuestions());
        allQuestions.add(new ArrayList<>());
    }

    // EFFECTS: initializes all questions of the history category
    public List<Question> initDefaultHistoryQuestions() {
        List<Question> historyQuestions = new ArrayList<>();
        Question q1 = new Question("Who was the first president?",0,"George Washington","Easy","History");
        Question q2 = new Question("What was the bloodiest war?", 1, "World War 2", "Medium", "History");
        Question q3 = new Question("Who's the father of Computer Science?", 2, "Alan Turing", "Hard", "History");
        Question q4 = new Question("What device revolutionized communication?", 3, "Telephone", "Easy", "History");
        Question q5 = new Question("When was the first Olympic Games?", 4, "1896", "Hard", "History");
        historyQuestions.add(q1);
        historyQuestions.add(q2);
        historyQuestions.add(q3);
        historyQuestions.add(q4);
        historyQuestions.add(q5);
        return historyQuestions;
    }

    // EFFECTS: initializes all questions of the science category
    public List<Question> initDefaultScienceQuestions() {
        List<Question> scienceQuestions = new ArrayList<>();
        Question q1 = new Question("What is the chemical formula for iron?", 5, "Fe", "Easy", "Science");
        Question q2 = new Question("What is the nearest planet to the sun?", 6, "Mercury", "Medium", "Science");
        Question q3 = new Question("What is the rarest blood type?", 7, "AB-", "Hard", "Science");
        Question q4 = new Question("What’s the boiling point of water?", 8, "100°C", "Easy", "Science");
        Question q5 = new Question("How long does a human red blood cell survive?", 9, "120 days", "Hard", "Science");
        scienceQuestions.add(q1);
        scienceQuestions.add(q2);
        scienceQuestions.add(q3);
        scienceQuestions.add(q4);
        scienceQuestions.add(q5);
        return scienceQuestions;
    }

    // MODIFIES: this
    // EFFECTS: sets the value of user-created questions
    public void setAllUserQuestions(List<Question> loq) {
        int counter = 0;
        if (loq.size() >= allQuestions.get(2).size()) {
            for (Question q : loq) {
                counter++;
                EventLog.getInstance().logEvent(new Event("The question " + q.getQuestion() + " has been loaded."));
                EventLog.getInstance().logEvent(new Event("Incorrect answers #" + counter + " has been loaded"));
            }
        } else {
            EventLog.getInstance().logEvent(new Event("User question has been removed."));
        }
        allQuestions.set(2, loq);
    }


    // EFFECTS: returns the history category questions
    public List<Question> getHistoryQuestions() {
        return allQuestions.get(0);
    }

    // EFFECTS: returns the science category questions
    public List<Question> getScienceQuestions() {
        return allQuestions.get(1);
    }

    // MODIFIES: this
    // EFFECTS: adds the user-created question
    public void addCustomQuestion(Question q) {
        allQuestions.get(2).add(q);
        EventLog.getInstance().logEvent(new Event(q.getQuestion() + " has been added to the question bank."));
    }

    // EFFECTS: returns unmodifiable list of the user created questions in the question bank
    public List<Question> getAllUserCreatedQuestions() {
        return Collections.unmodifiableList(allQuestions.get(2));
    }

    // EFFECTS: returns modifiable list of user created questions
    public List<Question> getAllUserCreatedQuestionsModifiable() {
        return allQuestions.get(2);
    }

    // EFFECTS: returns the user created questions according to category
    public List<Question> getUserCreatedQuestions(String category) {
        List<Question> requestedQuestions = new ArrayList<>();
        for (Question q : allQuestions.get(2)) {
            if (category.equals(q.getCategory())) {
                requestedQuestions.add(q);
            }
        }
        return requestedQuestions;
    }

    // EFFECTS: turns the question into a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Question", questionToJson());
        return json;
    }

    // EFFECTS: returns the user-created questions as a JSON array
    private JSONArray questionToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Question q : getUserCreatedQuestions("History")) {
            jsonArray.put(q.toJson());
        }

        for (Question q : getUserCreatedQuestions("Science")) {
            jsonArray.put(q.toJson());
        }

        return jsonArray;
    }

}
