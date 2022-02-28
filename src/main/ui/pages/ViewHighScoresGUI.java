package ui.pages;

import model.EventLog;
import model.ScoreBoard;
import ui.ScreenPrinter;
import persistence.JsonReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// The GUI page to view high scores
public class ViewHighScoresGUI extends JFrame implements ActionListener {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 600;
    private static final String JSON_STORE = "./data/scoreboard.json";
    private Boolean darkModeOn;
    private JsonReader jsonReader;
    private ScoreBoard scoreBoard;
    private List<String> scores;
    private BufferedImage img;
    private JButton backBtn;
    private JPanel backBtnPane;
    private JPanel scoresPane;
    private JLabel highScoresTitle;
    private JLabel first;
    private JLabel second;
    private JLabel third;
    private JLabel fourth;
    private JLabel fifth;

    // Constructor
    public ViewHighScoresGUI(Boolean darkMode) {
        super("Trivia Game");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("./data/highScoresLogo.jpg"));
        jsonReader = new JsonReader(JSON_STORE);
        darkModeOn = darkMode;
        loadScoreboard();
        scores = new ArrayList<>();
        loadScores();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Referenced https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ScreenPrinter.printLog(EventLog.getInstance());
            }
        });
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initEverything();
        setDarkMode();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: loads in the scoreboard from the JSON file
    private void loadScoreboard() {
        try {
            scoreBoard = jsonReader.read();
        } catch (IOException e) {
            scoreBoard = new ScoreBoard("High Scores");
        }
    }

    // MODIFIES: this
    // EFFECTS: helper to initialize everything
    private void initEverything() {
        backBtn = initBackBtn();
        initBufferedImage();
        initBackBtnPane(backBtn);
        initScoresPane();
        Container contentPane = getContentPane();
        contentPane.add(backBtnPane, BorderLayout.PAGE_START);
        contentPane.add(scoresPane, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: helper to initialize image
    private void initBufferedImage() {
        try {
            img = ImageIO.read(new File("data/trophy.jpg"));
        } catch (IOException e) {
            System.out.println("yes");
        }
    }

    // MODIFIES: this
    // EFFECTS: sets dark mode settings if user requests dark mode
    private void setDarkMode() {
        if (darkModeOn) {
            backBtnPane.setBackground(TriviaAppGUI.darkModeBackgroundColour);
            scoresPane.setBackground(TriviaAppGUI.darkModeBackgroundColour);
            setAllTextToWhite();
            setBackButtonToDarkMode();
        }
    }

    // MODIFIES: this
    // EFFECTS: sets text to white colour
    private void setAllTextToWhite() {
        highScoresTitle.setForeground(Color.WHITE);
        backBtn.setForeground(Color.WHITE);
        first.setForeground(Color.WHITE);
        second.setForeground(Color.WHITE);
        third.setForeground(Color.WHITE);
        fourth.setForeground(Color.WHITE);
        fifth.setForeground(Color.WHITE);
    }

    // MODIFIES: this
    // EFFECTS: turns the back button into dark mode settings
    private void setBackButtonToDarkMode() {
        backBtn.setBackground(TriviaAppGUI.darkModeButtonColour);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
    }

    // MODIFIES: this
    // EFFECTS: initializes back button panel
    private void initBackBtnPane(JButton backBtn) {
        backBtnPane = new JPanel();
        backBtnPane.add(backBtn);
        backBtnPane.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    // MODIFIES: this
    // EFFECTS: loads scoreboard from JSON
    private void loadScores() {
        for (int i = 0; i < 5; i++) {
            if (scoreBoard.getScores().size() > i) {
                if (scoreBoard.getScores().get(i) != null) {
                    scores.add(scoreBoard.getScores().get(i).toString());
                }
            } else {
                scores.add("No data yet");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes scoreboard panel
    private void initScoresPane() {
        scoresPane = new JPanel();
        highScoresTitle = new JLabel(scoreBoard.getName() + ":", SwingConstants.CENTER);
        highScoresTitle.setFont(new Font(highScoresTitle.getFont().getName(), Font.PLAIN, 30));
        JLabel picture = new JLabel(new ImageIcon(img));
        first = new JLabel("1. " + scores.get(0) + " points", SwingConstants.CENTER);
        second = new JLabel("2. " + scores.get(1) + " points", SwingConstants.CENTER);
        third = new JLabel("3. " + scores.get(2) + " points", SwingConstants.CENTER);
        fourth = new JLabel("4. " + scores.get(3) + " points", SwingConstants.CENTER);
        fifth = new JLabel("5. " + scores.get(4) + " points", SwingConstants.CENTER);
        scoresPane.setLayout(new GridLayout(7,0));
        scoresPane.add(highScoresTitle);
        scoresPane.add(picture);
        scoresPane.add(first);
        scoresPane.add(second);
        scoresPane.add(third);
        scoresPane.add(fourth);
        scoresPane.add(fifth);
    }

    // MODIFIES: this
    // EFFECTS: initializes back btn button
    private JButton initBackBtn() {
        JButton backBtn = new JButton("Back");
        backBtn.setActionCommand("backBtn");
        backBtn.addActionListener(this);
        return backBtn;
    }

    // EFFECTS: a back button to return to home screen
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("backBtn")) {
            TriviaAppGUI triviaGUI = new TriviaAppGUI(darkModeOn);
        }
        dispose();
    }
}
