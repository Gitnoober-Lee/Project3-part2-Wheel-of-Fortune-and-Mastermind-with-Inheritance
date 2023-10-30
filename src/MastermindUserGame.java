import java.util.Scanner;

public class MastermindUserGame extends Mastermind {

    public static void main(String[] args) {
        MastermindUserGame mastermindUserGame = new MastermindUserGame();
        AllGamesRecords record = mastermindUserGame.playAll();
        System.out.println(record);
    }

    @Override
    GameRecord play() {
        GameRecord record = new GameRecord();
        record.setPlayerId("Human");
        record.setScore(playingProgress());
        return record;
    }

    @Override
    boolean playNext() {
        System.out.println("Continue play? (Y/N): ");
        Scanner scanner = new Scanner(System.in);
        char input = Character.toLowerCase(scanner.nextLine().charAt(0));
        while (input != 'y' && input != 'n') {
            System.out.println("Please enter Y/y or N/n to continue playing: ");
            input = Character.toLowerCase(scanner.nextLine().charAt(0));
        }
        if (input == 'y') {
            return true;
        }
        return false;
    }

}
