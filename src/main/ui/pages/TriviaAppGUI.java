package ui.pages;

import model.EventLog;
import ui.ScreenPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// This class references code from the TrafficLight, DrawingEditor lecture repos
// GUI for the home screen
public class TriviaAppGUI extends JFrame implements ActionListener {
    public static final Color darkModeBackgroundColour = Color.decode("#181818");
    public static final Color darkModeButtonColour = Color.decode("#212121");
    private static final int WIDTH = 700;
    private static final int HEIGHT = 600;
    private boolean darkModeOn;
    private JPanel namePane;
    private JPanel buttonPane;
    private JPanel triviaTitlePane;
    private JLabel nameLabel;
    private JLabel triviaTitle;
    private JButton darkModeBtn;
    private JButton setNameBtn;
    private JButton quitBtn;
    private JButton viewButton;
    private JButton playButton;
    private JButton addQuestionButton;
    private Container contentPane;
    private JTextField nameTextField;

    // Constructor
    public TriviaAppGUI(Boolean darkModeOn) {
        super("Trivia Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Referenced https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ScreenPrinter.printLog(EventLog.getInstance());
            }
        });
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("./data/tobs.jpg"));
        initializeEverything(darkModeOn);
        darkMode();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: helper method to help with initializing everything
    private void initializeEverything(Boolean darkModeOn) {
        initAllButtons();
        triviaTitle = new JLabel("TriviaMania", SwingConstants.CENTER);
        triviaTitle.setFont(new Font(triviaTitle.getFont().getName(), Font.PLAIN, 50));
        initDarkButtonAndTriviaTitlePane();
        initNamePane(setNameBtn);
        initButtonPane(quitBtn, playButton, viewButton, addQuestionButton);
        contentPane = getContentPane();
        contentPane.add(triviaTitlePane, BorderLayout.PAGE_START);
        contentPane.add(namePane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        this.darkModeOn = darkModeOn;
    }

    // MODIFIES: this
    // EFFECTS: helper to initialize all buttons
    private void initAllButtons() {
        setNameBtn = initSetNameButton();
        quitBtn = initQuitButton();
        viewButton = initViewButton();
        playButton = initPlayButton();
        addQuestionButton = initAddQuestionButton();
    }

    // MODIFIES: this
    // EFFECTS: initiates trivia title and dark mode button panel and adds the two
    private void initDarkButtonAndTriviaTitlePane() {
        initDarkModeButton();
        triviaTitlePane = new JPanel();
        triviaTitlePane.add(triviaTitle);
    }

    // MODIFIES: this
    // EFFECTS: sets TriviaAppGUI into dark mode
    private void darkMode() {
        if (darkModeOn) {
            triviaTitle.setForeground(Color.WHITE);
            nameLabel.setForeground(Color.WHITE);
            contentPane.setBackground(darkModeBackgroundColour);
            namePane.setBackground(darkModeBackgroundColour);
            buttonPane.setBackground(darkModeBackgroundColour);
            triviaTitlePane.setBackground(darkModeBackgroundColour);
            darkModeButtonBackgroundColour(true);
            darkModeButtonSetTextColor(Color.WHITE);
        } else {
            revertEverythingToNormal();
        }
    }

    // MODIFIES: this
    // EFFECTS: changes everything back into default colour
    private void revertEverythingToNormal() {
        triviaTitle.setForeground(null);
        nameLabel.setForeground(null);
        contentPane.setBackground(null);
        namePane.setBackground(null);
        buttonPane.setBackground(null);
        triviaTitlePane.setBackground(null);
        darkModeButtonSetTextColor(null);
        darkModeButtonBackgroundColour(false);
        changeButtonsBackToNormalColour();
    }

    // MODIFIES: this
    // EFFECTS: helper method to change the text of buttons to white
    private void darkModeButtonSetTextColor(Color desiredColor) {
        setNameBtn.setForeground(desiredColor);
        quitBtn.setForeground(desiredColor);
        viewButton.setForeground(desiredColor);
        playButton.setForeground(desiredColor);
        addQuestionButton.setForeground(desiredColor);
        darkModeBtn.setForeground(desiredColor);
    }

    // MODIFIES: this
    // EFFECTS: changes buttons back to default colours
    private void changeButtonsBackToNormalColour() {
        setNameBtn.setBackground(null);
        quitBtn.setBackground(null);
        viewButton.setBackground(null);
        playButton.setBackground(null);
        addQuestionButton.setBackground(null);
        darkModeBtn.setBackground(null);
    }

    // MODIFIES: this
    // EFFECTS: helper method that makes the buttons dark mode
    private void darkModeButtonBackgroundColour(Boolean darkModeOnSettings) {
        setNameBtn.setBackground(darkModeButtonColour);
        setNameBtn.setOpaque(darkModeOnSettings);
        setNameBtn.setBorderPainted(!darkModeOnSettings);
        quitBtn.setBackground(darkModeButtonColour);
        quitBtn.setOpaque(darkModeOnSettings);
        quitBtn.setBorderPainted(!darkModeOnSettings);
        viewButton.setBackground(darkModeButtonColour);
        viewButton.setOpaque(darkModeOnSettings);
        viewButton.setBorderPainted(!darkModeOnSettings);
        playButton.setBackground(darkModeButtonColour);
        playButton.setOpaque(darkModeOnSettings);
        playButton.setBorderPainted(!darkModeOnSettings);
        addQuestionButton.setBackground(darkModeButtonColour);
        addQuestionButton.setOpaque(darkModeOnSettings);
        addQuestionButton.setBorderPainted(!darkModeOnSettings);
        darkModeBtn.setBackground(darkModeBackgroundColour);
        darkModeBtn.setOpaque(darkModeOnSettings);
        darkModeBtn.setBorderPainted(!darkModeOnSettings);
    }

    // MODIFIES: this
    // EFFECTS: initializes the set name field and button panel
    private void initNamePane(JButton setNameBtn) {
        nameLabel = new JLabel("Name:");
        nameTextField = new JTextField(10);
        namePane = new JPanel();
        namePane.setPreferredSize(new Dimension(WIDTH, HEIGHT / 2));
        namePane.add(darkModeBtn);
        namePane.add(Box.createRigidArea(new Dimension(WIDTH, 0)));
        namePane.add(Box.createRigidArea(new Dimension(0,HEIGHT - (2 * HEIGHT / 3))));
        namePane.add(nameLabel);
        namePane.add(Box.createRigidArea(new Dimension(WIDTH, -90)));
        namePane.add(nameTextField);
        namePane.add(setNameBtn);
    }

    // MODIFIES: this
    // EFFECTS: initializes the buttons panel
    private void initButtonPane(JButton quitBtn, JButton playButton, JButton viewButton, JButton addQuestionButton) {
        buttonPane = new JPanel();
        buttonPane.setLayout(new GridLayout(4,0));
        buttonPane.setPreferredSize(new Dimension(WIDTH, HEIGHT / 2));
        buttonPane.add(playButton);
        buttonPane.add(addQuestionButton);
        buttonPane.add(viewButton);
        buttonPane.add(quitBtn);
    }

    // MODIFIES: this
    // EFFECTS: initiates dark mode button
    private void initDarkModeButton() {
        darkModeBtn = new JButton("Dark Mode");
        darkModeBtn.setActionCommand("DarkModeButton");
        darkModeBtn.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: initializes the set name button
    private JButton initSetNameButton() {
        JButton btn = new JButton("Set Name");
        btn.setActionCommand("NameChangeButton");
        btn.addActionListener(this);
        return btn;
    }

    // MODIFIES: this
    // EFFECTS: initializes a quit button
    private JButton initQuitButton() {
        JButton btn = new JButton("Quit");
        btn.setActionCommand("QuitButton");
        btn.addActionListener(this);
        btn.setPreferredSize(new Dimension(WIDTH, 40));
        return btn;
    }

    // MODIFIES: this
    // EFFECTS: initializes the play button
    private JButton initPlayButton() {
        JButton btn = new JButton("Play");
        btn.setActionCommand("PlayButton");
        btn.addActionListener(this);
        return btn;
    }

    // MODIFIES: this
    // EFFECTS: initializes a view scores button
    private JButton initViewButton() {
        JButton btn = new JButton("Load and View Scores");
        btn.setActionCommand("ViewButton");
        btn.addActionListener(this);
        return btn;
    }

    // MODIFIES: this
    // EFFECTS: initializes an add question button
    private JButton initAddQuestionButton() {
        JButton btn = new JButton("Add a question");
        btn.setActionCommand("AddQuestionButton");
        btn.addActionListener(this);
        return btn;
    }

    // MODIFIES: this
    // EFFECTS: checks if the name is empty or not
    private void checkNameButton() {
        if (nameTextField.getText().isEmpty()) {
            nameLabel.setText("Please enter a name before playing");
        } else {
            GameSetupGUI gameSetupGUI = new GameSetupGUI(nameLabel.getText().substring(5), darkModeOn);
            dispose();
        }
    }

    // MODIFIES: this
    // EFFECTS: home screen to direct user to different GUI pages
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "NameChangeButton":
                nameLabel.setText("Name: " + nameTextField.getText());
                break;
            case "QuitButton":
                ScreenPrinter.printLog(EventLog.getInstance());
                System.exit(0);
            case "DarkModeButton":
                darkModeOn = !darkModeOn;
                darkMode();
                break;
            case "PlayButton":
                checkNameButton();
                break;
            case "ViewButton":
                ViewHighScoresGUI highScoresGUI = new ViewHighScoresGUI(darkModeOn);
                dispose();
                break;
            case "AddQuestionButton":
                AddQuestionGUI questionGUI = new AddQuestionGUI(darkModeOn);
                dispose();
                break;
        }
    }

}
