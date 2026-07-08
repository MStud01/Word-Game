package ui.gamemodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.DSL;
import model.DeterminedString;
import model.RSG;
import model.exceptions.DeterminedStringNotFoundException;

import ui.UserIO;

// In this game mode, the user can guess whether a randomly generated string of English letters is a
// word or not. 
// The user is presented a set of randomly generated strings where the size of the set and the length
// of each string is based on user inputs. The user is then asked to choose a string from the set and
// guess its word status and are immediately informed of the correctness of their guess.. 
// The user can also choose to pass their chances of guessing each string in the set if they find that
// the remaining unguessed strings in the set are not words.
// Right after the user is done guessing, they are presented with a short overview of their performance
// in the form of a letter grade based on their score for the round. The user is then prompted to enter 
// a secret message to enable cheats if they feel unsatisfied with their performance in the round,
// allowing to rectify their errors by chnaging the status of the generated strings in the DSL. This 
// secret message is inteded to be generated throughout the round or in one of the other game modes.
// Finally at the end of the round, the user is presented with a summary of their performance
// including their guesses, the actual word status of each of the generated strings in the set,
// whether each string was recently added to the DSL or had its word status changed in the round 
// due to cheats and a brief message describing their performance in the round enthusiastically. 

public class GuessingGameMode extends GameMode {
    static final int gameModeNum = 1;
    static final String gameModeTitle = "Guessing Mode";
    static final String gameMenuIntroMsg = "Guess whether a number of randomly generated string of English letters is a word or not.";
    
    private RSG rsg = RSG.getInstance();
    private DSL dsl = DSL.getInstance();
    private List<DeterminedString> generatedStrings;
    private List<String> addedStrings, changedStrings, guessedStrings;

    public GuessingGameMode() {
        this.rsg = RSG.getInstance();
        this.dsl = DSL.getInstance();
        this.generatedStrings = new ArrayList<DeterminedString>();
        this.addedStrings = new ArrayList<String>();
        this.changedStrings = new ArrayList<String>();
        this.guessedStrings = new ArrayList<String>();
    }

    @Override
    // This method initiates the "Guessing Mode" game mode, creates a new seed to generate new 
    // strings and prompts the user to make guesses on the word status of the newly generated strings 
    // displayed to them, informing them of the correctness of their guesses.
    // The user is then shown their score for the round and prompted for a secret message to be 
    // entered to enable cheats that allows them to change the word status of any of the generated strings.
    // The method makes calls to multiple methods, one generating new strings while
    // another allows the user to cheat their performance in the round and finally a couple of these
    // methods provide a detailed summary to the user of their progress and performance in the round.
    public void bootGameMode() {
        rsg.generateNewSeed();
        
        generateAndAddStrings();

        UserIO.INSTANCE.printToTerminal("Here is your set of generated strings:\n");
        int n = generatedStrings.size();
        for (int i = 0; i < n; i++) {
            UserIO.INSTANCE.printToTerminal(String.format("%-3s %-20s",Integer.toString(i + 1) + ".", generatedStrings.get(i).getString()) + ((i % 4 == 3) ? "\n" : " "));
        }

        UserIO.INSTANCE.printToTerminal("\n\nNow, you can guess whether each string is a word or not.\n");

        int score = 0;
        while (n > 0) {
            UserIO.INSTANCE.printToTerminal("Choose a string from the generated strings above and type it in to guess.\n");
            UserIO.INSTANCE.printToTerminal("Or you can type \"skip\" if you think the remainder of the strings cannot possibly be English words.\n");
            String response = UserIO.INSTANCE.scanner.nextLine();
            UserIO.INSTANCE.printToTerminal("\n");
            if (response.equalsIgnoreCase("skip")) {
                for (int j = 0; j < generatedStrings.size(); j++) {
                    DeterminedString ds = generatedStrings.get(j);
                    if (!guessedStrings.contains(ds.getString())) {
                        if (dsl.getDS(ds.getString()).isWord() == ds.isWord()) {
                            score++;
                        }
                        // TODO: Move the below line to the logging class once that is implemented
                        UserIO.INSTANCE.printToTerminal("You have skipped the string " + ds.getString() + ".\n");
                    }
                }
                UserIO.INSTANCE.printToTerminal("\n");
                // TODO: Add a delay here to allow the user to read which strings were skipped if the 
                // game speed is a bit too fast
                break;
            }
            String selectedString = response;
            int DSindex = generatedStrings.indexOf(new DeterminedString(selectedString, false)); 
            if (DSindex == -1) {
                UserIO.INSTANCE.printToTerminal("That was not one of the randomly generated strings. Try typing it again.\n");
                continue;
            }
            UserIO.INSTANCE.printToTerminal("Do you think this string is a word? Type \"Yes\" if you do think so. Else type \"No\".\n");
            String guess = UserIO.INSTANCE.scanner.nextLine();
            UserIO.INSTANCE.printToTerminal("\n");

            DeterminedString ds = generatedStrings.get(DSindex);
            if ((guess.toLowerCase().charAt(0) == 'y') || (guess.toLowerCase().charAt(0) == 'w')) {
                UserIO.INSTANCE.printToTerminal("You have guessed that the string is a word.\n");
                ds.setStatus(true);
            } else if (guess.toLowerCase().charAt(0) == 'n') {
                UserIO.INSTANCE.printToTerminal("You have guessed that the string is not a word.\n");
                ds.setStatus(false);
            } else {
                UserIO.INSTANCE.printToTerminal("That is an invalid choice. Please choose between the provided options.\n");
                continue;
            }

            try {
                if (dsl.getDSstatus(selectedString) == ds.isWord()) {
                    UserIO.INSTANCE.printToTerminal("Your guess was correct!!\n\n");
                    score++;
                } else {
                    UserIO.INSTANCE.printToTerminal("Unfortunately, uour guess was incorrect...\n");
                    UserIO.INSTANCE.printToTerminal("The string you guessed was actually " + (ds.isWord() ? "not a word" : "a word") + ".\n\n");
                }
                guessedStrings.add(selectedString);
            } catch (DeterminedStringNotFoundException dsnfe) {
                UserIO.INSTANCE.printToTerminal("\nERROR: THIS WAS NOT SUPPOSED TO HAPPEN!!!!\n");
                continue;
            }
            printSummaryRoundInProgress();
            n--;
        }
        // TODO: Look into generating the random word while calling generateAndAddStrings() in the current round or 
        // somewhere else in the current round along the main progression line where the user is 
        // guaranteed to be exposed to the secret messge needed below. 
        // Or, the secret word could be in one of the other game modes (possibly streak mode)

        // TODO: Add a delay here to allow the user to read which strings were skipped since the following section can be a bit too fast
        UserIO.INSTANCE.printToTerminal("Alright. We are done with all the guesswork.\n");
        UserIO.INSTANCE.printToTerminal("Here's how well you did this round.\n\n");
        
        UserIO.INSTANCE.printToTerminal("You have been awarded a letter grade of " + LetterGrade.getLetterGrade(score, generatedStrings.size()) + " for your performance in this round.\n");
        UserIO.INSTANCE.printToTerminal(String.format("Now that means you got a percentage of %.2f in this round\n\n", (((double) score/(double) generatedStrings.size()) * 100)).replace(" i","% i"));

        UserIO.INSTANCE.printToTerminal("Are you satisfied with your current grade?\n");
        UserIO.INSTANCE.printToTerminal("If not, type the secret message if you want to enable cheats on.\n");
        String message = UserIO.INSTANCE.scanner.nextLine();
        UserIO.INSTANCE.printToTerminal("\n");

        // TODO: Update the secret message to the one that is generated when it gets implemented
        if (message.contentEquals("message")) {
            secretCheats();
        } else {
            UserIO.INSTANCE.printToTerminal("TOOOOOOO BAD!! You missed your chance.\nHINT: Try looking for it in one of the other game modes.\n\n");
        }

        UserIO.INSTANCE.printToTerminal("That is the end of this round for the Guessing Game Mode.\n\n");
        UserIO.INSTANCE.printToTerminal("END OF ROUND SUMMARY:\n");
        printSummary();
    }

    // A helper method that generates random strings of characters resembling English words, where the
    // length and number of strings to be generated is defined by the user. The randomly generated 
    // strings are then indivdually handled depending on each of the following obscure cases:
    //              the string generated is already contained in the list of generated strings
    //              or in the case where the generated string is not found in the DSL
    // and then individually added to the the generatedStrings field.
    // REQUIRES: dsl.getDS(selectedString) != null
    // MODIFIES: generatedStrings, addedStrings, dsl
    // EFFECTS: Generates a number of random strings of characters and adds it to the generatedStrings
    private void generateAndAddStrings() {
        Random rng = new Random();
        UserIO.INSTANCE.printToTerminal("How large do you should the set of strings be?\n");
        int setSize = Integer.parseInt(UserIO.INSTANCE.scanner.nextLine());
        UserIO.INSTANCE.printToTerminal("\n");

        for (int i = 0; i < setSize; i++) {
            // The Idea is not to type in a number greater than 20 since it breaks formatting when printing
            // Type in a string that is not longer than 20 English letters.
            UserIO.INSTANCE.printToTerminal("Pick a number between 1 and 20.\n");
            int len = Integer.parseInt(UserIO.INSTANCE.scanner.nextLine());
            UserIO.INSTANCE.printToTerminal("\n");
            String generatedString = rsg.generateString(len);
            DeterminedString ds = new DeterminedString(generatedString, false);
            while (generatedStrings.contains(ds)) {
                generatedString = rsg.generateString(len);
                ds = new DeterminedString(generatedString, false);
            }
            generatedStrings.add(ds);
            try {
                dsl.getDSstatus(generatedString);
            } catch (DeterminedStringNotFoundException dsnfe) {
                dsl.addDS(generatedString, rng.nextBoolean());
                addedStrings.add(generatedString);
                UserIO.INSTANCE.printToTerminal("The string " + generatedString + " was not found in the in-game collection, and has now been added to the in-game\ncollection with a randomized status.\n\n");
            }
        }
        UserIO.INSTANCE.printToTerminal("\n");
    }

    @Override
    // This function prints a summary of the guesses made by the user as changes are made to 
    // the DSL (the in-game library), particularly relating to the strings generated this 
    // round, whether if one of them was recently added or had their word status changed.
    protected void printSummaryRoundInProgress() {
        for (int i = 0; i < generatedStrings.size(); i++) {
            DeterminedString ds = generatedStrings.get(i);
            UserIO.INSTANCE.printToTerminal((i + 1) + ". " + ds.getString() + "\n");
            if (guessedStrings.contains(ds.getString())) {
                UserIO.INSTANCE.printToTerminal("Your Guess - "+ (ds.isWord() ? "A Word" : "Not A Word") +"\n");
            }
            if (addedStrings.contains(ds.getString())) {
                // TODO: Move the below line to the logging class once that is implemented
                UserIO.INSTANCE.printToTerminal("This string was recently added into the in-game library in this round.\n");
            }
            UserIO.INSTANCE.printToTerminal("\n");
        }
    }

    // A secret helper method that runs when the user inputs the correct secret message to enable cheats.
    // This method allows the user to change the status of any of the generated strings in the DSL.
    // Currently the generation of the secret message has not been implemented and is static, while 
    // the intended functionality is to have the secret message be generated throughout the same round
    // or in one of the other game modes.
    // The following method was only present as way to debug promptForChanegStatus() and printSummary() methods
    private void secretCheats() {
        UserIO.INSTANCE.printToTerminal("OK. YOU FOUND IT!\nYou got lucky... maybe.\n\n");

        boolean changeFlag = true;
        String loopChoice = "noorp";

        do {
            if (changeFlag) {
                printSummary();
                changeFlag = false;
            }
            UserIO.INSTANCE.printToTerminal("Please type in the string that you wish to change the status of.\n");
            UserIO.INSTANCE.printToTerminal("Or if you would like to change the status of all the strings, you can type \"change all\".\n");
            String response = UserIO.INSTANCE.scanner.nextLine();
            UserIO.INSTANCE.printToTerminal("\n");
            if (response.toLowerCase().startsWith("change all")) {
                int prevCStrings = changedStrings.size();
                promptForChangeAllStatus();
                changeFlag = (changedStrings.size() != prevCStrings);
            } else {
                String selectedString = response;
                if (generatedStrings.indexOf(new DeterminedString(selectedString, false)) == -1) {
                    UserIO.INSTANCE.printToTerminal("That was an invalid string. Try typing in one of the generated strings below.\n\n");
                    changeFlag = true;
                    continue;
                }
                try {
                    boolean status = dsl.getDSstatus(selectedString);
                    int prevCStrings = changedStrings.size();
                    promptforChangeStatus(selectedString, !status);
                    changeFlag = (changedStrings.size() != prevCStrings);
                } catch (DeterminedStringNotFoundException dsnfe) {
                    UserIO.INSTANCE.printToTerminal("\nERROR: THIS WAS NOT SUPPOSED TO HAPPEN!!!!\n");
                }
            }
            UserIO.INSTANCE.printToTerminal("Is that all??? If you do still wish to make any more changes, type \"continue\".\n");
            UserIO.INSTANCE.printToTerminal("Or if you are done being a cheat, type \"I am happy to have been cheating and will now proudly declare my\nbrilliant score to be a result of me cheating\".\n");
            loopChoice = UserIO.INSTANCE.scanner.nextLine();
            UserIO.INSTANCE.printToTerminal("\n");
        }
        while ((loopChoice.toLowerCase().charAt(0) == 'n') || (loopChoice.toLowerCase().charAt(0) == 'c'));
    }

    // A secret helper method that changes the status of determined strings in the DSL that matches all
    // the generated strings in this round.
    // The user is asked whether they would like to change the status of the all the generated strings 
    // to be words. The status of the all the generated strings are changed accordingly as per the user's
    // choice, unless the user gives an invalid input where the status of the strings does not change in 
    // the DSL.
    // When a valid input has been entered by the user, each of the generated strings have their word 
    // status in the DSL changed to the desired status and are added to the changedStrings field, unless
    // the new status matches the status of the string in the DSL prior to this method call where none of
    // the aforementioned changes take place
    // The user is then informed if the status of the strings were changed successfully and that they are
    // able to make the changes to all the strings at a later time.
    private void promptForChangeAllStatus() {
        UserIO.INSTANCE.printToTerminal("Would you like to change all the strings to be words?\n");
        String prompt = UserIO.INSTANCE.scanner.nextLine();
        UserIO.INSTANCE.printToTerminal("\n");
        if (!(prompt.toLowerCase().startsWith("y")) && !(prompt.toLowerCase().startsWith("n"))) {
            UserIO.INSTANCE.printToTerminal("That is an invalid choice. The status of the strings will not be changed.\n\n");
        } else {
            boolean newStatus = prompt.toLowerCase().startsWith("y");
            for (DeterminedString ds : generatedStrings) {
                String selectedString = ds.getString();
                try {
                    if (dsl.getDSstatus(selectedString) != newStatus){
                        changedStrings.add(selectedString);
                        dsl.getDS(selectedString).setStatus(newStatus);
                    }
                } catch (DeterminedStringNotFoundException e) {
                    UserIO.INSTANCE.printToTerminal("\nTHIS WAS NOT SUPPOSED TO HAPPEN.\n");
                    break;
                }
            }
            UserIO.INSTANCE.printToTerminal("The status of all the generated strings were changed successfuly to " + (newStatus ? "" : "not ") + "be words.\n");
        }
        UserIO.INSTANCE.printToTerminal("The status of the strings can be changed at the end of the game or in the second game mode.\n\n");
    }

    // A secret helper method that changes the status of a determined string in the DSL depending on the user's input.
    // The user is asked whether they would like to change the status of the given string to a 
    // particular word status determined by the given boolean. The user is then informed of the 
    // result of their choice and are informed that they are able to make this change at a later time.
    // The method adds the given selectedString to the list of changedStrings if the user proceeds
    // to change the status of the determined string matching the given string.
    // REQUIRES: dsl.getDS(selectedString) != null
    // MODIFIES: changedStrings,  dsl (more specifically dsl.getDS(selectedString))
    // EFFECTS: Changes the status of a determined string in the DSL depending on the user's input
    private void promptforChangeStatus(String selectedString, boolean newStatus) {
        UserIO.INSTANCE.printToTerminal("Would you like to change the status of the string " + selectedString + " to " + (newStatus ? "" : "not ") + "be a word?\n");
        String prompt = UserIO.INSTANCE.scanner.nextLine();
        UserIO.INSTANCE.printToTerminal("\n");

        if ((prompt.toLowerCase().charAt(0) == 'y')) {
            UserIO.INSTANCE.printToTerminal("You have chosen to change the status of this string.\n");
            dsl.getDS(selectedString).setStatus(newStatus);
            changedStrings.add(selectedString);
        } else if (prompt.toLowerCase().charAt(0) == 'n') {
            UserIO.INSTANCE.printToTerminal("You have chosen to not change the status of this string.\n");
        } else {
            UserIO.INSTANCE.printToTerminal("That is an invalid choice. The status of this string will not be changed.\n");
        }

        UserIO.INSTANCE.printToTerminal("The status of the string " + selectedString + " can be changed at the end of the game or in the second game mode.\n\n");
    }

    @Override
    // This function prints the final summary after all the guessed are made as well as at
    // the end of the round. The function informs the user on their guesses and their total 
    // score. It also displays whether the string existed in the DSL at the start of the 
    // round and if its word status recorded in the DSL was changed in this round.
    protected void printSummary() {
        int score = 0, n = generatedStrings.size();
        for (int i = 0; i < n; i++) {
            DeterminedString ds = generatedStrings.get(i);
            try {
                // TODO: Find a way to change the font colors so it is easier to differentiate what part is the user's guess
                // and what part is the actual word status of the string
                UserIO.INSTANCE.printToTerminal(String.format("%-3s String %-32s   is\t\t%s\n", Integer.toString(i + 1)+".", ds.getString(), (dsl.getDSstatus(ds.getString()) ? "a Word" : "Not a Word")));
                UserIO.INSTANCE.printToTerminal(String.format("    You guessed that %-22s   is\t\t%s\n", ds.getString(),(ds.isWord() ? "a Word" : "Not a Word")));
                UserIO.INSTANCE.printToTerminal("    Recently added in this round?\t\t       "+ (addedStrings.contains(ds.getString()) ? "YES": "NO") +"\n");
                UserIO.INSTANCE.printToTerminal("    Changed status in this round:\t      " + (changedStrings.contains(ds.getString()) ? "Changed from " + (dsl.getDSstatus(ds.getString()) ? "not a word to a word" : "a word to not a word") :"Unchanged word status") +"\n\n");
                score += (dsl.getDSstatus(ds.getString()) == ds.isWord() ? 1 : 0);
            } catch (DeterminedStringNotFoundException dsnfe) {
                UserIO.INSTANCE.printToTerminal("\nTHIS SHOULD NOT BE PRINTED LIKE EVER.\n");
            }
        }
        UserIO.INSTANCE.printToTerminal("Your score for this round of Guessing Mode is " + score + " out of " + n +".\n");
        UserIO.INSTANCE.printToTerminal("Your Grade for this round: \t\t" + LetterGrade.getLetterGrade(score, n) + "\n");
        UserIO.INSTANCE.printToTerminal("Your peformance this round was" + getPerformanceMessage(LetterGrade.getLetterGrade(score, n)) + "\n\n");
    }

    // An inner enum consisting of all possible letter grades that can be received by the user using a 
    // custom grading scale that bounds each of the enum constant's lower and upper fields. 
    // This enum is only useful for this top-level class where it is used to get a performance message
    // based on the scores achieved by the user.
    // However, implementing this as an enum is still extremely useful as it allows a strict syntax when
    // using switch blocks to check for specific conditions and opens up the possible options to use it 
    // in other top-level classes, particularly the concrete classes extending GameMode
    private enum LetterGrade {
        S(0.90, 1.00),
        A(0.70, 0.90),
        B(0.50, 0.70),
        C(0.30, 0.50),
        D(0.05, 0.30),
        F(0.00, 0.05);

        private double lower;
        private double upper;

        private LetterGrade(double lower, double upper) {
            this.lower = lower;
            this.upper = upper;
        }

        // This method returns a letter grade enum constant using the given ints score and n, where
        //                                  score is the number of correct guesses made by the user, and
        //                                  n is the highest score possible in this round of the game mode. 
        private static LetterGrade getLetterGrade(int score, int n) {
            double delta = 0.000001, scoreOutOfTotal = (double) score/(double) n;
            for (LetterGrade lg : LetterGrade.values()) {
                if ((((scoreOutOfTotal - lg.lower) >= delta) || (Math.abs(scoreOutOfTotal - lg.lower) <= delta)) && ((lg == S) ? true : (scoreOutOfTotal < lg.upper))) {
                    return lg;
                }
            }
            // in case none of the conditions above return true
            return F;
        }
        // TODO: Override the toString() method to print the letter grades with color to the terminal
    }

    // A helper method that returns a message stating the user's performance for the round using the given
    // letterGrade enum constant. 
    private String getPerformanceMessage(LetterGrade letterGrade) {
        String performanceMessage;
        switch (letterGrade) {
            case S:
                performanceMessage = " SUPEEEEEEEEEERRRRRRRRRB!!!!! Really Great Job For Reaching The Peak.";
                break;
            case A:
                performanceMessage = " excellent. I am sure that with practice, You Will Reach The Top Soon!";
                break;
            case B:
                performanceMessage = " statistically above average. You displayed a Pretty Well-Versed Vocabulary..";
                break;
            case C:
                performanceMessage = " pretty.......   bad. You can do even better next time, I Am Sure.";
                break;
            case D:
                performanceMessage = "...... EH! I am sure that was a fluke. No way you are actually.... THIS BAD!!";
                break;
            default:
                performanceMessage = "..............ehem~... WOW!!!! I am IMPRESSED to say the least. You defied all odds\nto achieve such an...... *softly* unbelievably low ...  score that I am concerned\nif this is how you decide the actions in your Life.";
        }
        return performanceMessage;
    }

    @Override
    // This method prints the summary for debugging. Currently, the method just calls printSummary()
    // as that implementation seems detailed enough. Though, it does need to be looked into.
    protected void printSummaryDebug() {
        // TODO: Figure out whether DebugSummary will be differnt from EndOfRoundSummary
        printSummary();
    }
}
