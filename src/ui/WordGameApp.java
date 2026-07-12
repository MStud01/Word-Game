package ui;

import model.DSL;
import ui.gamemodes.GameMode;
import ui.gamemodes.GameModes;

// TODO: Add a logging system to log user inputs and game outcomes
// TODO: Add a saving system to save the user's progress and the list of Determined Strings (DSL) to a file

// TODO: Look into whether the GameModes enum needs to be a top-level enum or if it can be an inner enum of WordGameApp.

// An application that utilizes the Scanner class to create a command-line
// interface for the Word Guessing Game, allowing the user to  
//                      1) determine whether a set of randomly generated strings is an actual word or not, and
//                      2) add new words to DSL (the list of Determined Strings)

public class WordGameApp {
    // private static final String DSLfile = "";
    private static DSL dsl = DSL.getInstance();
    private static GameModes[] gameModes = GameModes.values();
    private static int gameModeCount = gameModes.length;  

    /** Looked into not needing to hardcode the list of gamemodes and found some interesting
     * trove of user-defined libraries that improved on the Reflection API's capabilitie:
     * https://stackoverflow.com/questions/205573/at-runtime-find-all-classes-in-a-java-application-that-extend-a-base-class
     * 
     * One library of particular interest is the ClassGraph API worked on by Luke Hutchinson:
     * https://github.com/classgraph/classgraph which seems useful for deep hierarchical class
     * structures as it is capable of recursively find subclasses of a class.
     */

    private GameMode currGameMode; // TODOL Look into if this needs to be static

    public WordGameApp() {
        // loadDSL(dsl);
        UserSettings.initializeSettings();
        start();

        // TODO: Revise this block for loading DSL
        // while (true) {
        //     // System.out.println("Loading Determined Strings (DSL)...");
        //     // dsl.loadDSL();
        //     // if (dsl.getDSL().isEmpty()) {
        //     //     System.out.println("No Determined Strings found. Please add new words to DSL.");
        //     //     bootAddMode();
        //     // } else {
        //     //     break;
        //     // }
        //     start();
        // }
    }

    // Boots up the start menu where the user is introduced to the game and the game modes
    // and is prompted in the subsequent function call
    public void start() {
        UserIO.INSTANCE.printToTerminal("Welcome to the Word Game!\n\n\n");
        UserIO.INSTANCE.printToTerminal("You can play " + gameModeCount + " modes in this game:\n\n");
        for (int i = 1; i <= gameModeCount; i++) {
            GameModes gm = gameModes[i - 1];
            UserIO.INSTANCE.printToTerminal("Game Mode " + i + ": " + gm.getTitle() + "\n" + gm.getGameMenuIntroMsg() + "\n\n");
        }
        UserIO.INSTANCE.printToTerminal("To play, choose between the above game modes.\n");
        processInputs();
    }

    // The user is prompted to choose among the previously presented options in start() and
    // the appropriate message is printed and a game mode is booted up through a function call
    // or the program exits according to the user's input, unless the inputs do not match any
    // of the presented options, upon which the user is prompted until the right input
    // has been entered by the user.
    public void processInputs() {
        String input = "message";

        while (!input.equalsIgnoreCase("quit")) {
            UserIO.INSTANCE.printToTerminal("What game mode do you wish to play?\n");
            UserIO.INSTANCE.printToTerminal("Type \"quit\" here, if you do not wish to play the game.\n");
            input = UserIO.INSTANCE.scanner.nextLine();
            UserIO.INSTANCE.printToTerminal("\n");

            if (input.matches("^[1-"+ Integer.toString(gameModeCount) +"]$")) {
                GameModes gm = gameModes[Integer.parseInt(input) - 1];
                UserIO.INSTANCE.printToTerminal("You have chosen the " + gm.getTitle() + ".\n");
                currGameMode = gm.getConstructor().get();
                currGameMode.bootGameMode();
            } else {
                for (GameModes gm : gameModes) {
                    // TODO: Consider changing the game mode names to ones excluding the -ing suffixes
                    // Or uncomment the commented if condition below
                    // if ((input.equalsIgnoreCase(gm.getTitle().substring(0, gm.getTitle().indexOf(" ")))) || (input.equalsIgnoreCase(gm.getTitle()))) { 
                    String expectedInputString = gm.getTitle().indexOf("ing") == -1 ? gm.getTitle().substring(0, gm.getTitle().indexOf(" ")) : gm.getTitle().substring(0, gm.getTitle().indexOf("ing"));
                    if ((input.equalsIgnoreCase(expectedInputString)) || (input.equalsIgnoreCase(gm.getTitle()))) {              
                        UserIO.INSTANCE.printToTerminal("You have chosen the " + gm.getTitle() + ".\n");
                        currGameMode = gm.getConstructor().get();
                        currGameMode.bootGameMode();
                        break;
                    }
                }
                if ((!input.equalsIgnoreCase("quit")) && (currGameMode == null)) {
                    UserIO.INSTANCE.printToTerminal("That is an invalid choice. Please choose between the provided options.\n");
                }
            }
            currGameMode = null;
        }

        UserIO.INSTANCE.printToTerminal("CHOICE: QUIT\n\n");
        UserIO.INSTANCE.printToTerminal("Thank you for playing the Word Guessing Game!\n");
        UserIO.INSTANCE.printToTerminal("Exiting the game now...");
    }
}