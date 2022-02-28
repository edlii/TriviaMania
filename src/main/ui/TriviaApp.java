package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// This class references code from these repos
// Link: https://github.students.cs.ubc.ca/CPSC210/TellerApp
// Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class TriviaApp {
    private static final String JSON_STORE = "./data/scoreboard.json";
    private static final String JSON_STORE_Q = "./data/userQuestions.json";
    private static final String JSON_STORE_IA = "./data/incorrectAnswers.json";
    TriviaGame triviaGame;
    private ScoreBoard scoreBoard;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriterQ;
    private JsonReader jsonReaderQ;
    private JsonWriter jsonWriterIA;
    private JsonReader jsonReaderIA;
    private Scanner scannerObj;
    private IncorrectAnswers incorrectAnswersBank;
    private QuestionBank questionBank;
    boolean isOn;
    int score;

    // Constructor
    public TriviaApp() {
        scoreBoard = new ScoreBoard("High Scores");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriterQ = new JsonWriter(JSON_STORE_Q);
        jsonReaderQ = new JsonReader(JSON_STORE_Q);
        jsonWriterIA = new JsonWriter(JSON_STORE_IA);
        jsonReaderIA = new JsonReader(JSON_STORE_IA);
        scannerObj = new Scanner(System.in);
        incorrectAnswersBank = new IncorrectAnswers();
        questionBank = new QuestionBank();
        loadScoreboard();
        runTriviaGame();
    }

    // EFFECTS: welcomes and initializes the TriviaGame
    public void runTriviaGame() {
        isOn = true;
        while (isOn) {
            System.out.print("Welcome to TriviaMania! Would you like to view scores, add questions, ");
            System.out.print("load and see user questions, or play the game?");
            System.out.println(" (view/play/add/see/quit)");
            String choice = scannerObj.nextLine();
            if (choice.equalsIgnoreCase("view")) {
                System.out.println("Here are the " + scoreBoard.getName() + ":");
                printScores();
            } else if (choice.equalsIgnoreCase("add")) {
                addQuestion();
            } else if (choice.equalsIgnoreCase("play")) {
                placeSettingsForTheGame();
                saveScoreboard();
            } else if (choice.equalsIgnoreCase("see")) {
                seeQuestions();
            } else if (choice.equalsIgnoreCase("quit")) {
                System.out.println("Thanks for playing! Goodbye!");
                isOn = false;
            }
        }
    }

    // MODIFIES: questionBank, incorrectAnswersBank
    // EFFECTS: helper to load user questions and incorrect answers
    private void loadQAndIA() {
        if (questionBank.getAllUserCreatedQuestions().size() == 0) {
            loadUserQuestions();
            loadUserIncorrectAnswers();
        }
    }

    // MODIFIES: triviaGame
    // EFFECTS: creates the settings for the game (difficulty and topics)
    private void placeSettingsForTheGame() {
        System.out.println("First, what is your first name?");
        String firstName = scannerObj.nextLine();
        System.out.println("Let's Begin! Please enter the category you would like to play (Science/History/Any):");
        String category = scannerObj.nextLine();
        System.out.println("Great! You will be asked " + category + " question(s)!");
        System.out.println("Now enter the difficulty you want to play (Easy/Medium/Hard/Any):");
        String difficulty = scannerObj.nextLine();
        System.out.println("Starting a " + difficulty + " " + category + " trivia game...");
        triviaGame = new TriviaGame(category, difficulty, new QuestionBank(), new IncorrectAnswers());

        playTheGame(firstName);
    }

    // MODIFIES: questionBank, incorrectAnswersBank
    // EFFECTS: prompts user to add a question to the question bank
    private void addQuestion() {
        loadQAndIA();
        System.out.println("What is the question?");
        String question = scannerObj.nextLine();
        System.out.println("What is the answer?");
        String answer = scannerObj.nextLine();
        System.out.println("What is the difficulty (Easy/Medium/Hard)");
        String difficulty = scannerObj.nextLine();
        System.out.println("What is the category (History/Science)");
        String category = scannerObj.nextLine();
        System.out.println("What is the first incorrect answer?");
        String incorrectAnswer1 = scannerObj.nextLine();
        System.out.println("What is the second incorrect answer?");
        String incorrectAnswer2 = scannerObj.nextLine();
        System.out.println("What is the third incorrect answer?");
        String incorrectAnswer3 = scannerObj.nextLine();
        int sizeOfIncorrectAnswers = incorrectAnswersBank.getIncorrectAnswers().size();
        Question q = new Question(question, sizeOfIncorrectAnswers, answer, difficulty, category);
        List<String> incorrectAnswers = new ArrayList<>();
        incorrectAnswers.add(incorrectAnswer1);
        incorrectAnswers.add(incorrectAnswer2);
        incorrectAnswers.add(incorrectAnswer3);
        save(q, incorrectAnswers);
    }

    // MODIFIES: userQuestions and incorrectAnswers JSON
    // EFFECTS: helper to save questions and custom incorrect answers
    private void save(Question question, List<String> incorrectAnswers) {
        System.out.println("Do you want to save the question? (y/n)");
        String choiceToSaveIntoJson = scannerObj.nextLine();
        if (choiceToSaveIntoJson.equals("y")) {
            questionBank.addCustomQuestion(question);
            incorrectAnswersBank.addIncorrectAnswers(incorrectAnswers);
            saveUserQuestions();
            saveUserIncorrectAnswers();
            System.out.println("Question has been saved into the data file!");
        } else {
            System.out.println("Question has not been saved");
        }
    }

    // EFFECTS: shows all user created questions
    private void seeQuestions() {
        loadQAndIA();
        if (questionBank.getAllUserCreatedQuestions().size() > 0) {
            System.out.println("Here are the questions:");
            System.out.println("--------------------------------");
            int counter = 1;
            for (Question q : questionBank.getAllUserCreatedQuestions()) {
                System.out.println(counter + ": " + q.getQuestion());
                counter++;
            }
            System.out.println("--------------------------------");
            removeQuestions();
        } else {
            System.out.println("No user questions made yet.");
        }
    }

    // REQUIRES: questionBank.getAllUserCreatedQuestions().size() > 0 && choice is a listed option
    // MODIFIES: userQuestions and incorrectAnswers JSON, questionBank, incorrectAnswersBank
    // EFFECTS: removes the question out of all three files/classes
    private void removeQuestions() {
        System.out.println("Would you like to remove any questions? Enter the number or enter any letter to quit:");
        String choice = scannerObj.nextLine();
        try {
            int index = Integer.parseInt(choice) - 1;
            int incorrectAnswersIndex = questionBank.getAllUserCreatedQuestions().get(index).getIncorrectAnswersIndex();
            incorrectAnswersBank.getIncorrectAnswers().remove(incorrectAnswersIndex);
            questionBank.getAllUserCreatedQuestionsModifiable().remove(index);
            decrementIncorrectAnswerIndex(incorrectAnswersIndex);
            saveUserQuestions();
            saveUserIncorrectAnswers();
            System.out.println("Question was successfully removed!");
        } catch (NumberFormatException e) {
            // return to main menu
        }
    }

    // MODIFIES: questionBank, Question
    // EFFECTS: decrements the incorrect answer index of all other indexes > given index after question is removed
    private void decrementIncorrectAnswerIndex(int index) {
        for (Question q : questionBank.getAllUserCreatedQuestions()) {
            if (q.getIncorrectAnswersIndex() > index) {
                q.setIncorrectAnswersIndex(q.getIncorrectAnswersIndex() - 1);
            }
        }
    }

    // EFFECTS: the game is played through here
    private void playTheGame(String name) {
        score = 0;
        Scanner scannerObj = new Scanner(System.in);
        List<String> orderedAnswers;
        for (Question q : triviaGame.getQuestions()) {
            System.out.println(q.getQuestion());
            orderedAnswers = triviaGame.produceOrderedAnswers(q);
            for (int i = 0; i < 4; i++) {
                System.out.println(orderedAnswers.get(i));
            }
            System.out.println("-- Write the letter below --");
            String choice = scannerObj.nextLine();
            String answer = getChoiceSentence(choice, orderedAnswers);
            if (triviaGame.checkCorrectAnswer(answer, q)) {
                score++;
                System.out.println("Correct! Your score is: " + score);
            } else {
                System.out.println("Incorrect! " + q.getCorrectAnswer() + " was the answer!");
            }
        }
        System.out.println("You scored " + score + " points!");
        scoreBoard.newHighScoreOrNot(name, score);
    }

    // EFFECTS: uses the choice to match with given answers (obtain the actual answer)
    private String getChoiceSentence(String s, List<String> answers) {
        String answer = "";
        for (String a : answers) {
            if (s.toUpperCase().equals(a.substring(0,1))) {
                answer = a.substring(3);
            }
        }
        return answer;
    }

    // MODIFIES: userQuestions JSON
    // EFFECTS: saves the userQuestions to file
    private void saveUserQuestions() {
        try {
            jsonWriterQ.open();
            jsonWriterQ.write(questionBank);
            jsonWriterQ.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE_Q);
        }
    }

    // MODIFIES: incorrectAnswersBank
    // EFFECTS: loads the user created questions from JSON file and adds it to this question bank
    private void loadUserQuestions() {
        try {
            questionBank.getAllUserCreatedQuestionsModifiable().addAll(jsonReaderQ.readQuestions());
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE_Q);
        }
    }

    // MODIFIES: incorrectAnswers JSON
    // EFFECTS: saves the user-created incorrect answers to file
    private void saveUserIncorrectAnswers() {
        try {
            jsonWriterIA.open();
            jsonWriterIA.write(incorrectAnswersBank);
            jsonWriterIA.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE_IA);
        }
    }

    // MODIFIES: incorrectAnswersBank
    // EFFECTS: gets all the user generated incorrect answers from JSON and adds it to the bank
    private void loadUserIncorrectAnswers() {
        try {
            List<List<String>> temp = jsonReaderIA.readIncorrectAnswers();
            incorrectAnswersBank.getIncorrectAnswers().addAll(temp);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE_IA);
        }
    }

    // MODIFIES: scoreboard JSON
    // EFFECTS: saves the scoreboard to file
    private void saveScoreboard() {
        try {
            jsonWriter.open();
            jsonWriter.write(scoreBoard);
            jsonWriter.close();
            System.out.println(scoreBoard.getName() + " have been updated and saved!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads scoreboard from file
    private void loadScoreboard() {
        try {
            scoreBoard = jsonReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: prints the scores in the scoreboard to the console
    private void printScores() {
        List<Score> scores = scoreBoard.getScores();

        for (Score s : scores) {
            System.out.println(s);
        }
    }
}
