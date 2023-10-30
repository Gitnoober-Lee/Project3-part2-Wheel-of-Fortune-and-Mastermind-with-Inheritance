import java.util.ArrayList;
import java.util.Collections;

public class AllGamesRecords {

    private ArrayList<GameRecord> recordList;

    public AllGamesRecords() {
        this.recordList = new ArrayList<>();
    }

    public double average() {
        double sum = 0;
        for (GameRecord record : this.recordList) {
            sum += record.getScore();
        }
        return sum / this.recordList.size();
    }

    public void add(GameRecord record) {
        this.recordList.add(record);
    }

    public double average(String playerId) {
        double sum = 0;
        int count = 0;
        for (GameRecord record : this.recordList) {
            if (record.getPlayerId() == playerId) {
                sum += record.getScore();
                count++;
            }
        }
        return (count != 0) ? sum / count++ : 0;
    }

    public ArrayList<GameRecord> highGameList(int n) {
        ArrayList<GameRecord> list = new ArrayList<>();
        Collections.sort(this.recordList);
        if (this.recordList.size() < n) {
            n = this.recordList.size();
        }
        for (int i = 0; i < n; i++) {
            list.add(this.recordList.get(i));
        }
        return list;
    }

    public ArrayList<GameRecord> highGameList(String playerId, int n) {
        ArrayList<GameRecord> list = new ArrayList<>();
        Collections.sort(this.recordList);
        int count = 0;
        int i = 0;
        while (count < n) {
            if (this.recordList.get(i).getPlayerId() == playerId) {
                list.add(this.recordList.get(i));
                count++;
            }
            i++;
        }
        return list;
    }

    @Override
    public String toString() {
        return "Highest score: " + highGameList(3)
                + "\r\nAverage score: " + average();
    }
}
