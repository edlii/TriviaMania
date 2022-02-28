package ui.pages;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.ScreenPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

// This class uses some code from Java docs
// The actual trivia game GUI page
public class GameGUI extends JFrame implements ActionListener {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 600;
    private static final String JSON_STORE = "./data/scoreboard.json";
    private static final Color correctColour = Color.decode("#88FF88");
    private static final Color incorrectColour = Color.decode("#D69191");
    private boolean darkModeOn;
    private int score;
    private ScoreBoard scoreBoard;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private TriviaGame currentGame;
    private String userPlaying;
    private JLabel question;
    private JLabel scoreLabel;
    private List<Question> questions;
    private Question currentQuestion;
    private Container contentPane;
    private JButton choiceA;
    private JButton choiceB;
    private JButton choiceC;
    private JButton choiceD;
    private JButton continueButton;
    private JPanel buttonPane;
    private JPanel continueAndScorePane;

    // Constructor
    public GameGUI(TriviaGame triviaGame, String name, Boolean darkMode) {
        super("Trivia Game");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("./data/playButton.jpg"));
        currentGame = triviaGame;
        userPlaying = name;
        darkModeOn = darkMode;
        // Referenced https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ScreenPrinter.printLog(EventLog.getInstance());
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initRestOfFieldsAndSetupPane();
        setDarkMode();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        initFirstQuestion();
    }

    // MODIFIES: this
    // EFFECTS: helper to set rest of fields and panel up
    private void initRestOfFieldsAndSetupPane() {
        score = 0;
        questions = currentGame.getQuestions();
        scoreBoard = new ScoreBoard("High Scores");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        placeAndInitChoiceButtons();
        contentPane = getContentPane();
        continueButton.setVisible(false);
        question = new JLabel("", SwingConstants.CENTER);
        question.setFont(new Font(question.getFont().getName(), Font.PLAIN, 20));
        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        initContinueAndScorePane();
        contentPane.add(continueAndScorePane, BorderLayout.PAGE_START);
        contentPane.add(question, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
    }

    // MODIFIES: this
    // EFFECTS: sets dark mode settings
    private void setDarkMode() {
        if (darkModeOn) {
            buttonPane.setBackground(TriviaAppGUI.darkModeBackgroundColour);
            continueAndScorePane.setBackground(TriviaAppGUI.darkModeBackgroundColour);
            contentPane.setBackground(TriviaAppGUI.darkModeBackgroundColour);
            setAllTextWhite();
            setAllButtonsDarkMode();
        }
    }

    // MODIFIES: this
    // EFFECTS: turns all text white
    private void setAllTextWhite() {
        question.setForeground(Color.WHITE);
        scoreLabel.setForeground(Color.WHITE);
        continueButton.setForeground(Color.WHITE);
        choiceA.setForeground(Color.WHITE);
        choiceB.setForeground(Color.WHITE);
        choiceC.setForeground(Color.WHITE);
        choiceD.setForeground(Color.WHITE);
    }

    // MODIFIES: this
    // EFFECTS: turns all buttons into dark mode buttons
    private void setAllButtonsDarkMode() {
        choiceA.setBackground(TriviaAppGUI.darkModeButtonColour);
        choiceB.setBackground(TriviaAppGUI.darkModeButtonColour);
        choiceC.setBackground(TriviaAppGUI.darkModeButtonColour);
        choiceD.setBackground(TriviaAppGUI.darkModeButtonColour);
        continueButton.setBackground(TriviaAppGUI.darkModeButtonColour);
        setButtonsOpaqueBorderPaintingSettings();
    }

    // MODIFIES: this
    // EFFECTS: helper method for general background opaque and border painting settings for macOS
    private void setButtonsOpaqueBorderPaintingSettings() {
        choiceA.setOpaque(true);
        choiceA.setBorderPainted(false);
        choiceB.setOpaque(true);
        choiceB.setBorderPainted(false);
        choiceC.setOpaque(true);
        choiceC.setBorderPainted(false);
        choiceD.setOpaque(true);
        choiceD.setBorderPainted(false);
        continueButton.setOpaque(true);
        continueButton.setBorderPainted(false);
    }

    // MODIFIES: this
    // EFFECTS: initializes continue button and score label panel and adds them
    private void initContinueAndScorePane() {
        continueAndScorePane = new JPanel();
        continueAndScorePane.add(scoreLabel);
        continueAndScorePane.add(continueButton);
    }

    // MODIFIES: this
    // EFFECTS: adds buttons to the button pane
    private void placeAndInitChoiceButtons() {
        initButtons();
        buttonPane = new JPanel();
        buttonPane.setLayout(new GridLayout(2,2));
        buttonPane.setPreferredSize(new Dimension(WIDTH, HEIGHT / 2));
        buttonPane.add(choiceA);
        buttonPane.add(choiceB);
        buttonPane.add(choiceC);
        buttonPane.add(choiceD);
    }

    // MODIFIES: this
    // EFFECTS: initializes buttons
    private void initButtons() {
        continueButton = new JButton("Continue");
        choiceA = new JButton("A");
        choiceB = new JButton("B");
        choiceC = new JButton("C");
        choiceD = new JButton("D");
        continueButton.setActionCommand("continueButton");
        continueButton.addActionListener(this);
        choiceA.setActionCommand("choiceA");
        choiceA.addActionListener(this);
        choiceB.setActionCommand("choiceB");
        choiceB.addActionListener(this);
        choiceC.setActionCommand("choiceC");
        choiceC.addActionListener(this);
        choiceD.setActionCommand("choiceD");
        choiceD.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: starts the game with the first question
    private void initFirstQuestion() {
        currentQuestion = questions.get(0);
        changeTextOfButtonsAndQuestion();
    }

    // MODIFIES: this
    // EFFECTS: checks selected answer and displays whether or not it is correct
    private void checkSelectedAnswer(JButton chosenBtn) {
        String answerChoice = chosenBtn.getText().substring(3);
        if (currentGame.checkCorrectAnswer(answerChoice, currentQuestion)) {
            chosenBtn.setBackground(correctColour);
            score++;
            scoreLabel.setText("Score: " + score);
        } else {
            showCorrectAnswerButton();
            chosenBtn.setBackground(incorrectColour);
        }
        chosenBtn.setOpaque(true);
        chosenBtn.setBorderPainted(false);
        disableButtons();
        continueButton.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: gets the next question in the list, removes from the list if more than one question still in list
    //          otherwise end game
    private void getNextQuestion() {
        continueButton.setVisible(false);
        if (questions.size() > 1) {
            questions.remove(0);
            currentQuestion = questions.get(0);
            changeTextOfButtonsAndQuestion();
            enableButtons();
        } else {
            int result = JOptionPane.showConfirmDialog(this,
                    "Want to save your scores?",
                    "Save",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                loadScoreboard();
                scoreBoard.newHighScoreOrNot(userPlaying, score);
                saveScoreboard();
            }
            TriviaAppGUI triviaAppGUI = new TriviaAppGUI(darkModeOn);
            dispose();
        }
    }

    // MODIFIES: this
    // EFFECTS: disables buttons from doing anything when clicked
    private void disableButtons() {
        choiceA.setActionCommand("");
        choiceB.setActionCommand("");
        choiceC.setActionCommand("");
        choiceD.setActionCommand("");
    }

    // MODIFIES: this
    // EFFECTS: enables buttons to have actions when clicked
    private void enableButtons() {
        choiceA.setActionCommand("choiceA");
        choiceB.setActionCommand("choiceB");
        choiceC.setActionCommand("choiceC");
        choiceD.setActionCommand("choiceD");
    }

    // MODIFIES: this
    // EFFECTS: resets settings to default and adds the next question's answers
    private void changeTextOfButtonsAndQuestion() {
        List<String> answers = currentGame.produceOrderedAnswers(currentQuestion);
        question.setText(currentQuestion.getQuestion());
        choiceA.setText(answers.get(0));
        choiceB.setText(answers.get(1));
        choiceC.setText(answers.get(2));
        choiceD.setText(answers.get(3));
        if (darkModeOn) {
            setAllButtonsDarkMode();
        } else {
            choiceA.setBackground(null);
            choiceA.setOpaque(false);
            choiceA.setBorderPainted(true);
            choiceB.setBackground(null);
            choiceB.setOpaque(false);
            choiceB.setBorderPainted(true);
            choiceC.setBackground(null);
            choiceC.setOpaque(false);
            choiceC.setBorderPainted(true);
            choiceD.setBackground(null);
            choiceD.setOpaque(false);
            choiceD.setBorderPainted(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays green hue on correct answer
    private void showCorrectAnswerButton() {
        String answerA = choiceA.getText().substring(3);
        String answerB = choiceB.getText().substring(3);
        String answerC = choiceC.getText().substring(3);
        JButton correctChoice;
        if (answerA.equals(currentQuestion.getCorrectAnswer())) {
            choiceA.setBackground(correctColour);
            correctChoice = choiceA;
        } else if (answerB.equals(currentQuestion.getCorrectAnswer())) {
            choiceB.setBackground(correctColour);
            correctChoice = choiceB;
        } else if (answerC.equals(currentQuestion.getCorrectAnswer())) {
            choiceC.setBackground(correctColour);
            correctChoice = choiceC;
        } else {
            choiceD.setBackground(correctColour);
            correctChoice = choiceD;
        }
        correctChoice.setOpaque(true);
        correctChoice.setBorderPainted(false);
    }

    // MODIFIES: ScoreBoard JSON file
    // EFFECTS: saves the scoreboard to file
    private void saveScoreboard() {
        try {
            jsonWriter.open();
            jsonWriter.write(scoreBoard);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads scoreboard from file
    private void loadScoreboard() {
        try {
            scoreBoard = jsonReader.read();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: mainly checks for correct answers and gets next question
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "choiceA":
                checkSelectedAnswer(choiceA);
                break;
            case "choiceB":
                checkSelectedAnswer(choiceB);
                break;
            case "choiceC":
                checkSelectedAnswer(choiceC);
                break;
            case "choiceD":
                checkSelectedAnswer(choiceD);
                break;
            case "continueButton":
                getNextQuestion();
                break;
        }
    }
}
