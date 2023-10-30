import java.util.Random;
import java.util.Scanner;

abstract public class Mastermind extends GuessingGame{
    static String colors = "RGBYOP";

    public int playingProgress(){
        int guessCount = 0;
        int partialCount;
        int exactCount;
        int score = 100;
        String secreteCode;
        String guessFromUser;

        secreteCode = generateSecret(colors);
        int countTry = 0;
        while (true) {
            StringBuilder secreteCodeForCrossingOut = new StringBuilder(secreteCode);
            guessFromUser = getGuess();
            exactCount = computeExacts(secreteCodeForCrossingOut, guessFromUser);
            partialCount = computePartials(secreteCodeForCrossingOut, guessFromUser);

            // Show results
            if (exactCount != 4) {
                System.out.println(exactCount + " exact, " + partialCount + "partial");
                guessCount++;
                countTry++;
            } else {
                System.out.println("Congratualations! You win in " + guessCount + " guesses.");
                break;
            }
        }

        return score-countTry;
    }


    // Generate random secret code
    public String generateSecret(String colors) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        int randomIndex;
        for (int i = 0; i < 4; i++) {
            randomIndex = rand.nextInt(6);
            sb.append(colors.charAt(randomIndex));
        }
        return sb.toString();
    }

    //User input
    // Red: R, Green: G, Blue: B, Yellow: Y, Orange: O, Purple: P
    public String getGuess() {
        System.out.println("Key in 4 colors[R,G,B,Y,O,P] in a row, and then press Enter.");
        Scanner scanner = new Scanner(System.in);
        StringBuilder guess = new StringBuilder();
        while (true) {
            guess.append(scanner.nextLine().toUpperCase());
            boolean isAllLetters = true;
            for (int i = 0; i < 4; i++) {
                if (!Character.isLetter(guess.charAt(i))) {
                    System.out.println("Please key in just letters:");
                    isAllLetters = false;
                    break;
                }
            }
            if (isAllLetters) {
                break;
            }
        }
        return guess.toString();
    }



    // Check exact matches
    public int computeExacts(StringBuilder secreteCodeForCrossingOut, String guess) {
        int exactCount = 0;
        for (int i = 0; i < 4; i++) {
            if (guess.charAt(i) == secreteCodeForCrossingOut.charAt(i)) {
                secreteCodeForCrossingOut.setCharAt(i, 'x');
                exactCount++;
            }
        }
        return exactCount;
    }

    // Check partial matches
    public int computePartials(StringBuilder secreteCodeForCrossingOut, String guess) {
        int partialCount = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != j) {
                    if (guess.charAt(i) == secreteCodeForCrossingOut.charAt(j)) {
                        secreteCodeForCrossingOut.setCharAt(j, 'x');
                        partialCount++;
                    }
                }
            }
        }
        return partialCount;
    }

}
