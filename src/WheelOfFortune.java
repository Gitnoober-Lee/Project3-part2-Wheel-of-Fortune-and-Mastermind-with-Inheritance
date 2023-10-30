import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Description:
 * @ClassName: WheelOfFortune
 * @Package: PACKAGE_NAME
 * @Author: Junpeng Li
 * @CreateTime: 10/24/23 8:22 PM
 */
public abstract class WheelOfFortune extends GuessingGame {

    protected String phrase = "";
    protected StringBuilder hiddenPhrase;
    protected char[] previousGuess = new char[20]; // Record all guess letters.
    protected boolean isAI = false;
    protected Scanner sc;
    private List<String> phraseList;
    protected String userName;
    protected AllGamesRecords allGameRecords;

// public static void main(String[] args) {
    //     System.out.println("=== Welcome to Wheel of Fortune!!! === \r\n" +
    //             "Please follow the following rules to play: \r\n" +
    //             "1. Guess a letter regardless of case and press enter key;\r\n" +
    //             "2. The game will calculate the guess and respond with related information for your reference;\r\n" +
    //             "3. According the response, take further actions until the game is over.\r\n");
    //
    //     WheelOfFortuneObject wfo = new WheelOfFortuneObject();
    //     phrase = wfo.randomPhrase();
    //     // For test only
    //     // System.out.println("original phrase: " + phrase);
    //     int cntAsterisk = 0; // Used for recording how many asterisks left.
    //     hiddenPhrase = new StringBuilder(phrase.toUpperCase());
    //     cntAsterisk = wfo.generateHiddenPhrase(cntAsterisk);
    //     System.out.println("Hidden phrase: " + hiddenPhrase);
    //     wfo.processGuess(phrase, hiddenPhrase, cntAsterisk);
    // }

    public WheelOfFortune() {
        allGameRecords = new AllGamesRecords();
        // Get the phrase from a file of phrases
        try {
            phraseList = Files.readAllLines(Paths.get("phrases.txt"));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * @Description: Return the initial hidden phrase
     * @Author Junpeng Li
     * @CreateTime 9/5/23 3:49 PM
     */
    public int generateHiddenPhrase(int cntAsterisk) {
        // Generate a hidden phrase.
        for (int i = 0; i < phrase.length(); i++) {
            char ch = phrase.charAt(i);
            if (Character.isAlphabetic(ch)) {
                hiddenPhrase.setCharAt(i, '*');
                cntAsterisk++;
            } else {
                hiddenPhrase.setCharAt(i, ch);
            }
        }
        return cntAsterisk;
    }

    /**
     * @Description: Get input from user and returns it.
     * @Author Junpeng Li
     * @CreateTime 9/5/23 3:49 PM
     */
    public char[] getGuess() {
        System.out.println("Please take a guess: ");
        return sc.next().toCharArray();
    }

    /**
     * @param cntAsterisk
     * @Description: Return whether a letter matches, and modify the partially hidden phrase if there is a match.
     * @Author Junpeng Li
     * @CreateTime 9/5/23 3:49 PM
     */
    public GameRecord processGuess(Integer cntAsterisk) { //should process singular task, keep 15-20 lines
        boolean run = true; // While loop control boolean.
        String upperPhrase = phrase.toUpperCase();
        int chances = 20; // Set guess chances.
        List<Character> prevMisses = new ArrayList<>(); // Record wrong guess letters.
        int count = 0; // Indicates how many characters are recorded in prevGuess char array.
        int missNum = 0; // Record the number of misses.
        // While loop, which is used for operate the main logic calculation.
        while (run) {
            boolean isMatch = false;
            char guessChar = getGuess(new String(previousGuess));
            checkAlphabet(guessChar, chances);
            //Make sure the current guess is not a letter that occurs in the guess history and ignore case.
            char upperInput = Character.toUpperCase(guessChar);
            boolean flag = checkRepeat(upperInput, guessChar);
            if (flag) {
                continue;
            }
            previousGuess[count] = guessChar;
            count++;
            chances--;
            for (int j = 0; j < upperPhrase.length(); j++) {
                if (upperPhrase.charAt(j) == upperInput) {
                    hiddenPhrase.setCharAt(j, phrase.charAt(j));
                    cntAsterisk--;
                    isMatch = true;
                }
            }
            if (!isMatch) {
                missNum++;
                prevMisses.add(guessChar);
            }
            printCurrentStatus(missNum, prevMisses, chances);
            run = isGameOver(cntAsterisk, chances);
        }
        return calculateResult(cntAsterisk, chances);
    }

    private GameRecord calculateResult(Integer cntAsterisk, int chances) {
        GameRecord g = new GameRecord();
        if (cntAsterisk == 0) {
            g.setScore(10);
            g.setPlayerId(userName);
        }
        if (chances == 0) {
            g.setScore(0);
            g.setPlayerId(userName);
        }
        return g;
    }


    private boolean isGameOver(Integer cntAsterisk, int chances) {
        if (cntAsterisk == 0) {
            System.out.println("Game over! You win!");
            System.out.println("The original phrase: " + phrase);
            return false;
        }
        if (chances == 0) {
            System.out.println("Game over! You lost!");
            System.out.println("The original phrase: " + phrase);
            return false;
        }
        return true;
    }

    private void checkAlphabet(char guessChar, int chances) {
        if (!Character.isAlphabetic(guessChar)) {
            System.out.println("Wrong input, only accept one letter! Your current input: " + guessChar);
            System.out.println("Your chances left: " + chances);
        }
    }

    private boolean checkRepeat(char upperInput, char guessChar) {
        for (int i = 0; i < previousGuess.length; i++) {
            if (previousGuess[i] == upperInput || previousGuess[i] == Character.toLowerCase(upperInput)) {
                System.out.println("Repeated input! Your current input: " + guessChar);
                System.out.println("---------------------------");
                return true;
            }
        }
        return false;
    }

    private void printCurrentStatus(int missNum, List<Character> prevMisses, int chances) {
        System.out.println("The current hidden code: " + hiddenPhrase.toString());
        System.out.println("The number of misses: " + missNum);
        System.out.println("Previous misses: " + prevMisses.toString());
        System.out.println("The remaining chances: " + chances);
        System.out.println("---------------------------");
    }

    public AllGamesRecords playAll() {
        printGreetings();
        for (String s : phraseList) {
            if (playNext()) {
                phrase = s;
                GameRecord record = play();
                allGameRecords.add(record);
                if (isAI) {
                    WheelOfFortunePlayer player = getPlayer();
                    player.reset();
                    previousGuess = new char[20];
                }
            } else {
                break;
            }
        }
        return allGameRecords;
    }

    private void printGreetings() {
        System.out.println("=== Welcome to Wheel of Fortune!!! === \r\n" +
                "Please follow the following rules to play: \r\n" +
                "1. Guess a letter regardless of case and press enter key;\r\n" +
                "2. The game will calculate the guess and respond with related information for your reference;\r\n" +
                "3. According the response, take further actions until the game is over.\r\n");
    }

    public GameRecord play() {
        int cntAsterisk = 0; // Used for recording how many asterisks left.
        hiddenPhrase = new StringBuilder(phrase.toUpperCase());
        cntAsterisk = generateHiddenPhrase(cntAsterisk);
        System.out.println("Hidden phrase: " + hiddenPhrase);
        return processGuess(cntAsterisk);
    }

    public boolean playNext() {
        if (isAI) {
            return true;
        } else {
            System.out.println("Do you want to play the following round? Y/N");
            String input = sc.next();
            if ("Y".equalsIgnoreCase(input)) {
                return true;
            } else {
                return false;
            }
        }
    }

    abstract void setUserName(String userName);

    abstract char getGuess(String previousGuesses);

    abstract WheelOfFortunePlayer getPlayer();

}
