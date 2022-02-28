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
import java.util.ArrayList;

// The GUI page to add custom questions
public class AddQuestionGUI extends JFrame implements ActionListener {
    private static final String JSON_STORE_Q = "./data/userQuestions.json";
    private static final String JSON_STORE_IA = "./data/incorrectAnswers.json";
    private static final int WIDTH = 700;
    private static final int HEIGHT = 600;
    private Boolean darkModeOn;
    private IncorrectAnswers incorrectAnswersBank;
    private QuestionBank questionBank;
    private JPanel backBtnPane;
    private JPanel addQuestionPane;
    private JToggleButton easyButton;
    private JToggleButton mediumButton;
    private JToggleButton hardButton;
    private JButton backBtn;
    private JButton removeQuestionBtn;
    private JTextField questionField;
    private JTextField answerField;
    private String difficulty;
    private String category;
    private JToggleButton scienceButton;
    private JToggleButton historyButton;
    private JTextField incorrectAnswer1Field;
    private JTextField incorrectAnswer2Field;
    private JTextField incorrectAnswer3Field;
    private JsonWriter jsonWriterQ;
    private JsonReader jsonReaderQ;
    private JsonWriter jsonWriterIA;
    private JsonReader jsonReaderIA;
    private JLabel incorrectAnswer1;
    private JLabel incorrectAnswer2;
    private JLabel incorrectAnswer3;
    private JLabel questionStatus;
    private JLabel question;
    private JLabel answer;
    private JLabel difficultyLabel;
    private JLabel categoryLabel;

    // Constructor
    public AddQuestionGUI(Boolean darkMode) {
        super("Trivia Game");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("./data/questionMark.jpg"));
        initializeEverything(darkMode);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Referenced https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ScreenPrinter.printLog(EventLog.getInstance());
            }
        });
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDarkMode();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: helper method to initialize everything
    private void initializeEverything(Boolean darkMode) {
        initJsonReaderWriters();
        darkModeOn = darkMode;
        incorrectAnswersBank = new IncorrectAnswers();
        questionBank = new QuestionBank();
        loadUserQuestions();
        loadUserIncorrectAnswers();
        backBtn = initBackBtn();
        JButton addQuestionBtn = initAddQuestionRemoveQuestionBtn();
        initBackBtnPane(backBtn);
        initAddQuestionPane(addQuestionBtn);
        Container contentPane = getContentPane();
        contentPane.add(backBtnPane, BorderLayout.PAGE_START);
        contentPane.add(addQuestionPane, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: initializes JsonReaders and writers
    private void initJsonReaderWriters() {
        jsonWriterQ = new JsonWriter(JSON_STORE_Q);
        jsonReaderQ = new JsonReader(JSON_STORE_Q);
        jsonWriterIA = new JsonWriter(JSON_STORE_IA);
        jsonReaderIA = new JsonReader(JSON_STORE_IA);
    }

    // MODIFIES: this
    // EFFECTS: sets dark mode
    private void setDarkMode() {
        if (darkModeOn) {
            backBtnPane.setBackground(TriviaAppGUI.darkModeBackgroundColour);
            addQuestionPane.setBackground(TriviaAppGUI.darkModeBackgroundColour);
            setAllTextToWhite();
            setBackButtonDarkMode();
        }
    }

    // MODIFIES: this
    // EFFECTS: sets all text to white
    private void setAllTextToWhite() {
        questionStatus.setForeground(Color.WHITE);
        question.setForeground(Color.WHITE);
        answer.setForeground(Color.WHITE);
        difficultyLabel.setForeground(Color.WHITE);
        categoryLabel.setForeground(Color.WHITE);
        incorrectAnswer1.setForeground(Color.WHITE);
        incorrectAnswer2.setForeground(Color.WHITE);
        incorrectAnswer3.setForeground(Color.WHITE);
        backBtn.setForeground(Color.WHITE);
    }

    // MODIFIES: this
    // EFFECTS: sets all buttons into dark mode
    private void setBackButtonDarkMode() {
        backBtn.setBackground(TriviaAppGUI.darkModeButtonColour);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
    }


    // MODIFIES: this
    // EFFECTS: initializes the back button panel
    private void initBackBtnPane(JButton backBtn) {
        backBtnPane = new JPanel();
        backBtnPane.add(backBtn);
        backBtnPane.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    // MODIFIES: this
    // EFFECTS: initializes back button
    private JButton initBackBtn() {
        JButton backBtn = new JButton("Back");
        backBtn.setActionCommand("backBtn");
        backBtn.addActionListener(this);
        return backBtn;
    }

    // MODIFIES: this
    // EFFECTS: initializes add question and remove question button
    private JButton initAddQuestionRemoveQuestionBtn() {
        removeQuestionBtn = new JButton("Load and see user-made questions");
        removeQuestionBtn.setActionCommand("removeQuestionButton");
        removeQuestionBtn.addActionListener(this);
        JButton addQuestionBtn = new JButton("Save question");
        addQuestionBtn.setActionCommand("addQuestionButton");
        addQuestionBtn.addActionListener(this);
        return addQuestionBtn;
    }

    // MODIFIES: this
    // EFFECTS: initializes add question panel
    private void initAddQuestionPane(JButton addQuestionBtn) {
        addQuestionPane = new JPanel();
        questionStatus = new JLabel("", SwingConstants.CENTER);
        addQuestionFields();
        addQuestionPane.add(Box.createRigidArea(new Dimension(WIDTH, 0)));
        addQuestionPane.add(addQuestionBtn);
        addQuestionPane.add(removeQuestionBtn);
    }

    // MODIFIES: this
    // EFFECTS: helper method to add all "question-adding" fields
    private void addQuestionFields() {
        questionField = new JTextField(20);
        answerField = new JTextField(20);
        initDifficultyButtons();
        initCategoryButtons();
        addEverythingToPanel();
        addIncorrectAnswersField();
        addQuestionPane.add(questionStatus);
    }

    // MODIFIES: this
    // EFFECTS: sets up the design of the GUI page
    private void addEverythingToPanel() {
        question = new JLabel("Question:");
        answer = new JLabel("Answer:");
        difficultyLabel = new JLabel("Difficulty:");
        categoryLabel = new JLabel("Category:");
        addQuestionPane.add(Box.createRigidArea(new Dimension(WIDTH, HEIGHT / 8)));
        addQuestionPane.add(question);
        addQuestionPane.add(questionField);
        addQuestionPane.add(Box.createRigidArea(new Dimension(WIDTH, 0)));
        addQuestionPane.add(answer);
        addQuestionPane.add(answerField);
        addQuestionPane.add(Box.createRigidArea(new Dimension(WIDTH, 0)));
        addQuestionPane.add(difficultyLabel);
        addQuestionPane.add(easyButton);
        addQuestionPane.add(mediumButton);
        addQuestionPane.add(hardButton);
        addQuestionPane.add(Box.createRigidArea(new Dimension(WIDTH, 0)));
        addQuestionPane.add(categoryLabel);
        addQuestionPane.add(scienceButton);
        addQuestionPane.add(historyButton);
        addQuestionPane.add(Box.createRigidArea(new Dimension(WIDTH, 0)));
    }

    // MODIFIES: this
    // EFFECTS: initializes the difficulty buttons
    private void initDifficultyButtons() {
        easyButton = new JToggleButton("Easy");
        mediumButton = new JToggleButton("Medium");
        hardButton = new JToggleButton("Hard");
        easyButton.setActionCommand("easyButton");
        easyButton.addActionListener(this);
        mediumButton.setActionCommand("mediumButton");
        mediumButton.addActionListener(this);
        hardButton.setActionCommand("hardButton");
        hardButton.addActionListener(this);
        ButtonGroup difficultyButtons = new ButtonGroup();
        difficultyButtons.add(easyButton);
        difficultyButtons.add(mediumButton);
        difficultyButtons.add(hardButton);
    }

    // MODIFIES: this
    // EFFECTS: initializes category buttons
    private void initCategoryButtons() {
        scienceButton = new JToggleButton("Science");
        historyButton = new JToggleButton("History");
        scienceButton.setActionCommand("scienceButton");
        scienceButton.addActionListener(this);
        historyButton.setActionCommand("historyButton");
        historyButton.addActionListener(this);
        ButtonGroup categoryButtons = new ButtonGroup();
        categoryButtons.add(scienceButton);
        categoryButtons.add(historyButton);
    }

    // MODIFIES: this
    // EFFECTS: initializes all incorrect answer related fields
    private void addIncorrectAnswersField() {
        incorrectAnswer1 = new JLabel("Incorrect Answer 1:");
        incorrectAnswer2 = new JLabel("Incorrect Answer 2:");
        incorrectAnswer3 = new JLabel("Incorrect Answer 3:");
        incorrectAnswer1Field = new JTextField(20);
        incorrectAnswer2Field = new JTextField(20);
        incorrectAnswer3Field = new JTextField(20);
        addQuestionPane.add(incorrectAnswer1);
        addQuestionPane.add(incorrectAnswer1Field);
        addQuestionPane.add(Box.createRigidArea(new Dimension(WIDTH, 0)));
        addQuestionPane.add(incorrectAnswer2);
        addQuestionPane.add(incorrectAnswer2Field);
        addQuestionPane.add(Box.createRigidArea(new Dimension(WIDTH, 0)));
        addQuestionPane.add(incorrectAnswer3);
        addQuestionPane.add(incorrectAnswer3Field);
        addQuestionPane.add(Box.createRigidArea(new Dimension(WIDTH, 0)));
    }

    // EFFECTS: turns incorrect answer inputs into a list
    private List<String> produceIncorrectAnswerSet() {
        List<String> incorrectAnswerSet = new ArrayList<>();
        String incorrectAnswer1 = incorrectAnswer1Field.getText();
        String incorrectAnswer2 = incorrectAnswer2Field.getText();
        String incorrectAnswer3 = incorrectAnswer3Field.getText();
        incorrectAnswerSet.add(incorrectAnswer1);
        incorrectAnswerSet.add(incorrectAnswer2);
        incorrectAnswerSet.add(incorrectAnswer3);
        return incorrectAnswerSet;
    }

    // MODIFIES: QuestionBank, IncorrectAnswers, Both JSON files
    // EFFECTS: adds and saves custom question to JSON
    private void addQuestion() {
        int sizeOfIncorrectAnswers = incorrectAnswersBank.getIncorrectAnswers().size();
        String question = questionField.getText();
        String answer = answerField.getText();
        String difficulty = this.difficulty;
        String category = this.category;
        Question q = new Question(question, sizeOfIncorrectAnswers, answer, difficulty, category);
        questionBank.addCustomQuestion(q);
        saveUserQuestions();
        incorrectAnswersBank.addIncorrectAnswers(produceIncorrectAnswerSet());
        saveUserIncorrectAnswers();
    }

    // MODIFIES: JSON, QuestionBank
    // EFFECTS: creates JOptionPane which allows user to select user-made questions and remove them
    // Referenced code from:
    // https://stackoverflow.com/questions/15363524/using-an-arraylist-with-joptionpane-showinputdialog
    // https://stackoverflow.com/questions/42725666/how-can-i-display-a-message-dialog-box-in-java
    private void removeQuestion() {
        List<Question> userQuestions = questionBank.getUserCreatedQuestions("History");
        userQuestions.addAll(questionBank.getUserCreatedQuestions("Science"));
        if (userQuestions.size() > 0) {
            List<String> questionNames = convertQuestionsIntoNames(userQuestions);
            Object[] options = questionNames.toArray();
            Object value = JOptionPane.showInputDialog(
                    null,
                    "Here are the questions. Select a question and press `Ok` if you want to remove it.",
                    "List of all Questions",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (value != null) {
                int index = questionNames.indexOf(value);
                removeTheQuestion(userQuestions.get(index), userQuestions);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No questions yet", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // MODIFIES: JSON, QuestionBank
    // EFFECTS: removes the question from user question and rewrites JSON
    private void removeTheQuestion(Question question, List<Question> userQuestions) {
        int badAnswerIndexOfQuestion = question.getIncorrectAnswersIndex();
        incorrectAnswersBank.getIncorrectAnswers().remove(badAnswerIndexOfQuestion);
        userQuestions.remove(question);
        for (Question q : userQuestions) {
            if (q.getIncorrectAnswersIndex() > badAnswerIndexOfQuestion) {
                q.setIncorrectAnswersIndex(q.getIncorrectAnswersIndex() - 1);
            }
        }
        saveUserIncorrectAnswers();
        questionBank.setAllUserQuestions(userQuestions);
        saveUserQuestions();
    }

    // EFFECTS: gets the question names of given list of questions
    private List<String> convertQuestionsIntoNames(List<Question> questions) {
        List<String> temp = new ArrayList<>();
        for (Question q : questions) {
            temp.add(q.getQuestion());
        }
        return temp;
    }

    // MODIFIES: userQuestions JSON file
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

    // MODIFIES: questionBank
    // EFFECTS: loads the user created questions from JSON file and adds it to this question bank
    private void loadUserQuestions() {
        try {
            questionBank.getAllUserCreatedQuestionsModifiable().addAll(jsonReaderQ.readQuestions());
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE_Q);
        }
    }

    // MODIFIES: incorrectAnswers JSON file
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

    // MODIFIES: this
    // EFFECTS: ensures no fields are empty before adding the question
    private void checkEmptyFields() {
        if (questionField.getText().isEmpty()) {
            questionStatus.setText("Please enter a non-empty question!");
        } else if (answerField.getText().isEmpty()) {
            questionStatus.setText("Please enter a non-empty answer!");
        } else if (difficulty == null) {
            questionStatus.setText("Please select a difficulty!");
        } else if (category == null) {
            questionStatus.setText("Please select a category!");
        } else if (checkIncorrectAnswerFields()) {
            questionStatus.setText("Please enter non-empty incorrect answers!");
        } else {
            addQuestion();
            questionStatus.setText("Question successfully added and saved!");
        }
    }

    // EFFECTS: returns true if incorrect answer fields are empty
    private boolean checkIncorrectAnswerFields() {
        boolean isEmpty = false;
        if (incorrectAnswer1Field.getText().isEmpty()) {
            isEmpty = true;
        } else if (incorrectAnswer2Field.getText().isEmpty()) {
            isEmpty = true;
        } else if (incorrectAnswer3Field.getText().isEmpty()) {
            isEmpty = true;
        }
        return isEmpty;
    }



    // MODIFIES: userQuestions incorrectAnswers JSON files, this
    // EFFECTS: adds proper field values for questions and listens to button actions to save questions
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("backBtn")) {
            TriviaAppGUI triviaGUI = new TriviaAppGUI(darkModeOn);
            dispose();
        } else if (e.getActionCommand().equals("easyButton")) {
            difficulty = "Easy";
        } else if (e.getActionCommand().equals("mediumButton")) {
            difficulty = "Medium";
        } else if (e.getActionCommand().equals("hardButton")) {
            difficulty = "Hard";
        } else if (e.getActionCommand().equals("scienceButton")) {
            category = "Science";
        } else if (e.getActionCommand().equals("historyButton")) {
            category = "History";
        } else if (e.getActionCommand().equals("addQuestionButton")) {
            checkEmptyFields();
        } else if (e.getActionCommand().equals("removeQuestionButton")) {
            removeQuestion();
        }
    }


}
