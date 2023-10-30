public abstract class GuessingGame {

    AllGamesRecords playAll() {
        AllGamesRecords allGamesRecord = new AllGamesRecords();
        while (playNext()) {
            allGamesRecord.add(play());
        }
        return allGamesRecord;
    }

    abstract boolean playNext();

    abstract GameRecord play();
}
