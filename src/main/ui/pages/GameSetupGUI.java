package ui.pages;

import model.*;
import ui.ScreenPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

// The GUI page for setting up a trivia game
public class GameSetupGUI extends JFrame implements ActionListener {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 600;
    private JPanel backBtnPane;
    private JPanel mainPane;
    private Boolean darkModeOn;
    private BufferedImage img;
    private JToggleButton easyButton;
    private JToggleButton mediumButton;
    private JToggleButton hardButton;
    private JToggleButton anyDifficultyButton;
    private JToggleButton scienceButton;
    private JToggleButton historyButton;
    private JToggleButton anyCategoryButton;
    private JLabel nameLabel;
    private JLabel difficultyLabel;
    private JLabel categoryLabel;
    private JButton backBtn;
    private JButton playButton;
    private ButtonGroup categoryButtons;
    private ButtonGroup difficultyButtons;
    private String name;
    private String difficulty;
    private String category;

    // Constructor
    public GameSetupGUI(String name, Boolean darkMode) {
        super("Trivia Game");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("./data/kahoot.jpg"));
        this.name = name;
        darkModeOn = darkMode;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Referenced https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ScreenPrinter.printLog(EventLog.getInstance());
            }
        });
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addEverythingToPanels();
        Container contentPane = getContentPane();
        contentPane.add(backBtnPane, BorderLayout.PAGE_START);
        contentPane.add(mainPane, BorderLayout.CENTER);
        setDarkModeOn();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: sets dark mode settings
    private void setDarkModeOn() {
        if (darkModeOn) {
            backBtnPane.setBackground(TriviaAppGUI.darkModeBackgroundColour);
            mainPane.setBackground(TriviaAppGUI.darkModeBackgroundColour);
            setAllTextToWhite();
            setButtonsDarkMode();
        }
    }

    // MODIFIES: this
    // EFFECTS: applies dark mode to the buttons
    private void setButtonsDarkMode() {
        backBtn.setBackground(TriviaAppGUI.darkModeButtonColour);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
    }

    // MODIFIES: this
    // EFFECTS: turns all text to white
    private void setAllTextToWhite() {
        nameLabel.setForeground(Color.WHITE);
        categoryLabel.setForeground(Color.WHITE);
        difficultyLabel.setForeground(Color.WHITE);
        backBtn.setForeground(Color.WHITE);
    }

    // MODIFIES: this
    // EFFECTS: initializes back button panel
    private void initBackBtnPane(JButton backBtn) {
        backBtnPane = new JPanel();
        backBtnPane.add(backBtn);
        backBtnPane.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    // MODIFIES: this
    // EFFECTS: initializes the back button
    private JButton initBackBtn() {
        JButton backBtn = new JButton("Back");
        backBtn.setActionCommand("backBtn");
        backBtn.addActionListener(this);
        return backBtn;
    }

    // MODIFIES: this
    // EFFECTS: initializes the play button
    private void initPlayButton() {
        playButton = new JButton("Play");
        playButton.setActionCommand("playButton");
        playButton.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: helper method to add everything to the main panels
    private void addEverythingToPanels() {
        backBtn = initBackBtn();
        initBackBtnPane(backBtn);
        initPlayButton();
        initCategoryButtons();
        initDifficultyButtons();
        mainPane = new JPanel();
        nameLabel = new JLabel("Name: " + name);
        difficultyLabel = new JLabel("Difficulty:");
        categoryLabel = new JLabel("Category:");
        addStuffToMainPane();
    }

    // MODIFIES: this
    // EFFECTS: helper to add stuff to the main pane
    private void addStuffToMainPane() {
        mainPane.add(Box.createRigidArea(new Dimension(WIDTH,HEIGHT / 5)));
        mainPane.add(nameLabel);
        mainPane.add(Box.createRigidArea(new Dimension(WIDTH,0)));
        mainPane.add(difficultyLabel);
        mainPane.add(easyButton);
        mainPane.add(mediumButton);
        mainPane.add(hardButton);
        mainPane.add(anyDifficultyButton);
        mainPane.add(Box.createRigidArea(new Dimension(WIDTH,0)));
        mainPane.add(categoryLabel);
        mainPane.add(scienceButton);
        mainPane.add(historyButton);
        mainPane.add(anyCategoryButton);
        mainPane.add(Box.createRigidArea(new Dimension(WIDTH,0)));
        mainPane.add(playButton);
        mainPane.add(Box.createRigidArea(new Dimension(WIDTH,0)));
    }

    // MODIFIES: this
    // EFFECTS: initializes the choose difficulty buttons
    private void initDifficultyButtons() {
        easyButton = new JToggleButton("Easy");
        mediumButton = new JToggleButton("Medium");
        hardButton = new JToggleButton("Hard");
        anyDifficultyButton = new JToggleButton("Any");
        easyButton.setActionCommand("easyButton");
        easyButton.addActionListener(this);
        mediumButton.setActionCommand("mediumButton");
        mediumButton.addActionListener(this);
        hardButton.setActionCommand("hardButton");
        hardButton.addActionListener(this);
        anyDifficultyButton.setActionCommand("anyDifficultyButton");
        anyDifficultyButton.addActionListener(this);
        difficultyButtons = new ButtonGroup();
        difficultyButtons.add(easyButton);
        difficultyButtons.add(mediumButton);
        difficultyButtons.add(hardButton);
        difficultyButtons.add(anyDifficultyButton);
    }

    // MODIFIES: this
    // EFFECTS: initializes the choose category buttons
    private void initCategoryButtons() {
        scienceButton = new JToggleButton("Science");
        historyButton = new JToggleButton("History");
        anyCategoryButton = new JToggleButton("Any");
        scienceButton.setActionCommand("scienceButton");
        scienceButton.addActionListener(this);
        historyButton.setActionCommand("historyButton");
        historyButton.addActionListener(this);
        anyCategoryButton.setActionCommand("anyCategoryButton");
        anyCategoryButton.addActionListener(this);
        categoryButtons = new ButtonGroup();
        categoryButtons.add(scienceButton);
        categoryButtons.add(historyButton);
        categoryButtons.add(anyCategoryButton);
    }

    // MODIFIES: this
    // EFFECTS: sets up the fields to initiate a new trivia game
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
        } else if (e.getActionCommand().equals("anyDifficultyButton")) {
            difficulty = "any";
        } else if (e.getActionCommand().equals("scienceButton")) {
            category = "Science";
        } else if (e.getActionCommand().equals("historyButton")) {
            category = "History";
        } else if (e.getActionCommand().equals("anyCategoryButton")) {
            category = "any";
        } else if (e.getActionCommand().equals("playButton")) {
            TriviaGame newGame = new TriviaGame(category, difficulty, new QuestionBank(), new IncorrectAnswers());
            GameGUI gameGUI = new GameGUI(newGame, name, darkModeOn);
            dispose();
        }
    }
}
