package ui;

import java.util.Scanner;

// A singleton class that creates an interactive terminal for the user to play the game.
// Each class in ui accesses the singleton instance to print outputs and accept user inputs.
// Currently, this class only allows text outputs and will output images in future versions.
public enum UserIO {
    INSTANCE; // singleton enum instance so all package classes can access its attr and methods

    // I PREFER BRIGHT COLORS RED, YELLOW AND GREEN OVER THEIR NOT-BRIGHT ALTS

    public static final String RESET = "\033[0m";
    public static final String BLACK_TEXT = "\033[0;30m";
    public static final String RED_TEXT = "\033[0;31m";
    public static final String GREEN_TEXT = "\033[0;32m";
    public static final String YELLOW_TEXT = "\033[0;33m";
    public static final String BLUE_TEXT = "\033[0;34m";
    public static final String MAGENTA_TEXT = "\033[0;35m";
    public static final String CYAN_TEXT = "\033[0;36m";
    public static final String WHITE_TEXT = "\033[0;37m";
    public static final String BRIGHT_RED_TEXT = "\033[0;91m";
    public static final String BRIGHT_GREEN_TEXT = "\033[0;92m";
    public static final String BRIGHT_YELLOW_TEXT = "\033[0;93m";
    public static final String BRIGHT_BLUE_TEXT = "\033[0;94m";
    public static final String BRIGHT_MAGENTA_TEXT = "\033[0;95m";
    public static final String BRIGHT_CYAN_TEXT = "\033[0;96m";
    public static final String BRIGHT_WHITE_TEXT = "\033[0;97m";
    public static final String BLACK_BG = "\033[0;40m";
    public static final String RED_BG = "\033[0;41m";
    public static final String GREEN_BG = "\033[0;42m";
    public static final String YELLOW_BG = "\033[0;43m";
    public static final String BLUE_BG = "\033[0;44m";
    public static final String MAGENTA_BG = "\033[0;45m";
    public static final String CYAN_BG = "\033[0;46m";
    public static final String WHITE_BG = "\033[0;47m";
    public static final String BRIGHT_RED_BG = "\033[0;101m";
    public static final String BRIGHT_GREEN_BG = "\033[0;102m";
    public static final String BRIGHT_YELLOW_BG = "\033[0;103m";
    public static final String BRIGHT_BLUE_BG = "\033[0;104m";
    public static final String BRIGHT_MAGENTA_BG = "\033[0;105m";
    public static final String BRIGHT_CYAN_BG = "\033[0;106m";
    public static final String BRIGHT_WHITE_BG = "\033[0;107m";

    public Scanner scanner = new Scanner(System.in);

    // A custom print method that utilizes Thread manipulation to print the given string 
    // character by character with a slight delay between each character being printed.
    public void printToTerminal(String message) {
        for (char c: message.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(50/UserSettings.gameSpeed);
                // gameSpeed is set by user when the game is run
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        // System.out.println(""); can be added if no formatting is used
    }
}
