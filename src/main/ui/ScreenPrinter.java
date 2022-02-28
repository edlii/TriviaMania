package ui;

import model.Event;
import model.EventLog;

// Class to contain the printLog method
public class ScreenPrinter {

    // EFFECTS: prints out all events to the terminal
    public static void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.getDescription());
        }
    }
}
