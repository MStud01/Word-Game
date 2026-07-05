## Why does this file exist?

In this file, I will post all changes and implementations I make and explain the idea and reasoning behind doing so in detail. This will help me keep track of my level of knowledge I display with each commit and also allows me to keep notes of ideas to implement in the future.
This will be different from the README.md doc in a way that shows what I have worked on in detail with each change/commit I have made.
This will be introduced for commits following the commit 36344df2e48cb18540b030b59d734e8ec4a188e5 made on June 28 2026 8:01 PM GMT +3.


## Project Design changes to make

Rework the word guessing game so it utilizes phonetics instead of chars and look into appropriate syllable compositions (like voiceless postalveolar fricative + mid front non-rounded vowel + voiceless alveolar stop, etc.)

There will exist 2 modes for this app:
First mode is where the player gets a set of randomly selected/generated words and the player is asked to identify for certain features of the words (like are they familiar with the word, do certain words match with a particular theme). This will be the general gameplay until the difficulty of the game can be adjusted (look into this further). 
Second mode is the build-up of the word library that the player can work with, where they are presented with a randomly generated word with the aforementioned appropriate syntax. 

I need to look into:
1) Proper syntax that makes the syllables of an English word
2) Adjusted difficulty for the first mode of the game
3) GUI elements that are available in Java (or C/C++)

The reason behind this change is that the initial idea seems stupid and uninteresting. It will be more appropriate to randomize IPA phonetics since they are easier to sound out.
Additionally, this could work well with another project that utilizes IPA phonetics for voice recognition.

## Changes made

### 28th June 2026 11:30 PM GMT +3
In this commit, I made the changes necessary in the GuessingGameMode class to make explicit the dependency relationship of the WordGameApp class on the concrete class extending the abstract GameMode class. This is one of a couple commits I will be making to resolve this issue.
- Changed the static-ity of fields rsg, dsl, generatedStrings, addedStrings and changedStrings to non-static in GuessingGameMode class
    - These fields had no reason to be static.
- Changed the static-ity of all methods in GuessingGameMode class
    - The methods should not have been static in the first place since I only needed to pass in the property of being able to call the bootGameMode() method of any class extending the GameMode class, which can be done by passing the constructor of the corresponding class instead. 
- Implemented a constructor for GuessingGameMode class
    - Now, I can pass into the GameModes enum that the WordGameApp class can access to run the bootGameMode() method.
- Removed redundant code and unnecessary commented TODOs in GuessingGameMode class
    - These lines and comments existed since I needed a fix the persistance of some of the fields that were static prior to this commit.
- Removed TODOs detailing the need for the above changes in the GuessingGameMode class
- Added TODOs for fixing the inherited methods in GuessingGameMode class
    - The inherited methods in GuessingGameMode class were given different names and their following methods in the abstract superclass (GameMode class) were not made abstract.
- Changed the visibility of the bootGameMode() method in GuessingGameMode class to public
    - This was done so the WordGameApp class could call the method.


### 29th June 2026 12:00 AM GMT +3
In this commit, I make the next batch of changes to allow the WordGameApp to properly construct the concrete subclass of GameMode in the GameModes enum. The rest of these changes will follow in the next commit.

I also made changes to the GameMode class (and its other concrete subclasses) to match the visibility and static-ity changes I made to the bootGameMode() method in GuessingGameMode class.
- Changed the passing of bootGameMode() to the passing of the corresponding constructor in GameModes enum
    - This is to make explicit the dependency that the WordGameApp class has on classes that extend the GameMode class. In the previous implementation, this was obscured unintentionally when I realized I could just pass the bootGameMode() method. Additionally, I was only focused on utilizing a piece of knowledge that I was not very familiar with as I had not been able to utilize this knowledge elsewhere in the past year.
- Changed the static-ity of the bootGameMode() method in GameMode class and the other concrete subclass that do not have complete implementations
    - So the compiler does not complain. And I had to push these changes along so I don't forget about it later.
- Changed the visibility of the bootGameMode() method in GameMode class and the other concrete subclass that do not have complete implementations to public
    - I made this change once I realized I would not be able to call the method in WordGameApp class. Same reason about the not wanting to push this later.
- Removed TODOs informing about the botched inheritance of the GameMode class
    - Yeah, the only concrete class for the GameMode class that has a complete implementation does not have its methods matching the ones that are listed in the GameMode class. So this was pointed out here. Moved this concern to the GuessingGameMode class where that change is more appropriately voiced.
- Added TODO about fixing about how the "abstract" methods in GameMode class are not abstract
    - So, I realized I did not make this change when I implemented this class at first. It only makes the compiler be more pissy about not having implemented it in the concrete subclasses, of which are there are a few that do not have complete implementations. And thus, I tacked this onto my TODOs.
- Fixed the indentation errors in this file.
    - I am not really familiar with the Markdown syntax.

### 30th June 2026 05:08 PM GMT +3 

Made the final batch of changes to allow the WordGameApp class to construct the GameMode, making clear the dependency relationship of this class on any concrete classes extending GameMode class.
- Changed the type of the currGameMode field from the GameModes enum type to the GameMode class type in the WordGameApp class.
    - Made this change to store the newly intialized GameMode concrete type, which varies depending on user input. So now it matches the type of class utilized to call bootGameMode() method.
- Introduced a local variable called gm of type GameMOdes enum in WordGameApp class (line 79)
    - So there doesn't need to be multiple accessing of the GameModes enum when the if block is true and it matches the local variable created in the else block.
- Changed which value currGameMode field gets at runtime in WordGameApp class (lines 81 to 82 and 92 to 93)
    - So now a newly initialized GameMode concrete object is utilized instead of a Runnable object that was passed into the GameModes enum. Regardless, the bootGameMode() method is still called.
- Changed the local variable that is used to print to terminal in WordGameApp class (line 80)
    - Previously, it used the GameModes enum field currGameMode. Now, it utilizes the GameModes enum local variable gm which is declared and initialized in the if block.
- Changed the static-ity of the currGameMode field to non-static in the WordGameApp class.
    - I don't really have a clue why I needed this to be static so I changed it back since it makes sense that it changes during runtime and it doesn't really need to be accessed.
- Switched around the lines that were printed to the console (lines 73 and 74) so it better fits the contents of the prints. 
- Added a TODO for the above change I made to the static-ity of the currGameMode field in WordGameApp class.
    - I am still a bit doubtful whether it DOES indeed NEED to be static. So, added this on in case something goes wrong in the future.

### 30th June 2026 05:36 PM GMT +3
So, I made the changes to fix the inheritance relationship between the abstract GameMode class and the concrete GuessingGameMode class. And Added a lot to consider for making the GuessingGameMode class more cohesive and the game a bit more fun.

- Swapped around the printSummary() method and printSummaryRoundInProgress() method in GameMode class
    - Makes more sense when printSummary() is run usually at the end of the round and should be after printSummaryRoundInProgress() method in the class file.
- Changed the names of printChangesSummary() method to printSummaryRoundInProgress() and printFinalSummary() method to printSummary() in GuessingGameMode class
    - Fixing the inheritance relationship between the abstract GameMode class and its concrete classes. So the names of the methods had to match
- Changed the visibility of printSummaryRoundInProgress() and printSummary() methods from private to protected in GuessingGameMode class.
    - The compiler complains a lot about not narrowing the visibility of the inherited methods regardless of whether the method is abstract or not in the superclass.
- Added a new method called printSummaryDebug() method in GuessingGameMode class
    - This is supposed to be inherited from the GameMode abstract class and so I am adding it here as well since it is useful for debugging purposes. Currently, it just calls printSummary().
- Added Override annotations for the inherited methods in GuessingGameMode class
- Switched around the print to Terminal at lines 173 and 174 to make it consistent with the rest of the prints with a similar pattern in GuessingGameMode class.
- Added a TODO of ending the round at an earlier point when all te guessing is done in GuessingGameMode class (lines 97 and 113).
    - I plan to add some secrets about the game that would allow the user to have cheats on. There is already a secret fast mode I added for setting game speed. Just need to leave breadcrumbs to get there.
    - Also need to consider if promptForChangeStatus() method would be needed if the above suggestion is implemened. 
- Added a TODO to look into figuring out a different implementation for printSummaryDebug() method in GuessingGameMode class
- Added a couple TODOs (at lines 175 and 260) to refactor/abstract out methods of cohesion in GuessingGameMode class
- Fixed a typo on a comment in GuessingGameMode class (line 169)
- Removed a comment that implied calling printSummaryRoundInProgress() instead of printSummary() in GuessingGameMode class (line 128)
    
### 30th June 2026 05:39 PM GMT +3
- Added a TODO to RSG class considering whether the RSG class needs to be a singleton class
- Added a TODO to README.md doc
    - I made quite a few changes to the model and have implemented a working but not complete ui since the last time I updated it. 

### 1st July 2026 11:00 PM GMT +3

- Added a helper method called getPerformanceMessage() in GuessingGameMode class
    - This method as added to make the printSummary() method more cohesive since it did not seem to follow the single responsibility, single method rule.
- Wrote specifications for the GuessingGameMode class
    - This explains what the user can do in this game mode as well as what information the user will
    be privy to throughout a round of this game mode.
- Wrote specifications for generateAndAddStrings(), promptforChangeStatus() and getPerformanceMessage() helper methods and printSummaryDebug() inherited method in GuessingGameMode class
    - The helper methods now explain their particular effects and some of these specifications also explain the pre-conditions for some of the methods that are required to be met prior to them being called.
    - The inherited method does not do much other than just call printSummary() so it does not really have a detailed specification as compared to the specifications for the other methods. I have already added it to my TODO list to figure out an implementation for this method that makes it unique.
- Removed a TODO regarding the refactoring of bootGameMode() method in GuessingGameMode class
    - I will remember it when I learn something new that I can use to refactor that method since it's commom for me to look for ways to apply a newly acquired skill.
- Added a TODO explaiing my concerns of returning a locally defined variable from within a helper method
    - This was added just recently along with the new helper method getPerformanceMessage() whose name also does not seem that special. 
- Added a TODO to implement a cheats mode for the user to chnage the status of any of the generated strings in GuessingGameMode class
    - Made quite a few alternative ideas regardingo how the cheats mode could be accessed by the user. This would be more appropriate since it does not make sense for the user to just be able to change the status of the strings in the in-game library. 
- Changed the wording of a TODO in GuessingGameMode class (line 108)

### 2nd July 2026 04:33 PM GMT +3
In this commit, I changed up the user's access to being able to change the status of any of the generated strings using a secret method with the idea that it is a cheat that the user can gain access to by finding and inputing a secret message hidden somewhere in the game.
I made some other changes that better fit this addition of hiding the accessibility of this cheat-like method removing lines wherever fit and added tasks onto my TODO list reminding myself to properly implement the generaton of the secret message for the secretCheats() method and to update other aspects of the GuessingGameMode class that were affected by this new addition.

- Added a method to enable the cheats mode for the user utilizing previously implemented code called secretCheats() method in GuessingGameMode class
    - This wmethod was added since it did not feel right that the user had access to changing the status of any generated string without any sort of restricted access to it. Now, it's hidden behind a secret message (that is static for now but it will be) generated or randomly selected witin the round or ine one of the other game modes to be implmented. 
- Added lines to better suit the tone when calling the secretCheats() method including an if block in GuessingGameMode class (lines 127, 132 to 137).
- Removed lines calling promptForChangeStatus() method in bootGameMode() method in GuessingGameMode class
    - These were removed since that functionality should be locked behind the secret cheat method which would be accessed after the user is done guessing the word status of the generated strings
- Removed local variable changeFlag and if block using the local variable in bootGameMode() method in GuessingGameMode class
    - Since the local variable is no longer being updated, one branch of the if condition block would never be run. Now, bootGameMode() method always calls printSummaryRoundInProgress() after a guess is made by the user.
- Swapped around the declaration and implementation of the prompForChangeStatus() method in the GuessingGameMode class
    - Since this method is now only called in the secretCheats() method, it is now located under it at line 246. 
- Swapped around the declaration and implementation of the printSymmaryRoundInProgress() method in the GuessingGameMode class
    - This method is called before the secretCheats() helper method in the bootGameMode() method, so it is now located under it at line 206. 
- Removed TODO regarding implementing the cheat method in GuessingGameMode class
- Removed TODO to update the user once all the guessing has been done in GuessingGameMode class
- Added TODO to generate the secret message for the secretCheats() method in GuessingGameMode class
    - The secret message would need to be generated throughout the current round of the game mode or be generated in one of the other game modes and stored so that it is remembered when the user tries to use it in this game mode.
- Added a TODO to update what information the user should be presented prior to the prompt asking for the secret message
    - IMO it would be better if the user does not know all the information about the generated strings as those would be shown in secretCheats() method or at the end of the round.
- Added a TODO reminding myself to update the if condition when the generation of the secret message has been implemented in GuessingGameMode class (line 131)
- Added TODOs to update specifications of the class the and bootGameMode() method in GuessingGameMode class
    - The specifications are a bit outdated since some of the logics and method to change the status of a generated string has been hidden behind a secret cheats method.

### 6TH July 2026 02:51 PM GMT +3
- Removed if block for checking updates in changedStrings in printSummaryRoundInProgress() method in GuessingGameMode class
    - The local variable changedStrings can no longer be updated when this method can be called during the section of the game mode wher the user can guess the word status due to the bfuscation/obscuring of the promptForChangeStatus() method.
- Updated specifications for the GuessingGameMode class and the bootGameMode() method inGuessingGameMode class 
    - They now reflect the correct flow of the game mode with the introduction of the secretCheats() method and obfuscation/obscuring of the promptForChangeStatus() method as well as the updated printSummary() and printSummaryRoundInProgress() methods.
- Added a line to print a message stating that the user can turn off cheats mode in secretCheats() in GuessingGameMode class
    - Now, the user is explicitly shown to type something other than continue to exit the loop which enables the cheats for the user for as long as they wish in secretCheats() method.
- Fixed bug where the program throws a IndexNotFoundException when checking the loop variable response in secretCheats() method in GuessingGameMode class
    - This was being triggered when the user fails to input a valid string when being prompted to type one of the generated strings and the loop variable does not update from an empty string. Now, the loop variable starts off being non-empty and satisfies the loop condition so the loop will continue running unless the user decides to turn off the cheats mode.
- Removed the early initialization of the list fields in GuessingGameMode class
    - These fields were already being initialized when the constructor for the class was being called. So, it didn't make sense for the initializtion to occur at the declaration of these fields as well.
- Changed name of the loop variable in secretCheats() method in GuessingGameMode class to loopChoice
    - Made this change ahead of future changes that I plan to make to this function
- Added a TODO to allow the user to change the status of all the generated strings at once through the secretCheats() method in GuessingGameMode class
- Added a TODO to change the output text color to easily differentiate the user's guess from the actual word status in printSummary() in GuessingGameMode class
- Removed the TODOs that notified me to update the specifications of the class and bootGameMode() method in GuessingGameMode class 


### Next commit : DATE TIME
- Changed functionality of secretCheats() method
- Added a field called guessedStrings and a local variable called score in bootGameMode() method in GuessingGameMode class
- Create an inner enum for letterGrades in GuessingGameMode class 


General template
### Next commit : DATE TIME
- Changes in ???