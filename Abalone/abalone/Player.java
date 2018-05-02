package abalone;
import java.util.ArrayList;

public class Player {
    private Board board;
    private ArrayList<Marble> marbles;
    private int killCount;

    public Player(Board board, ArrayList<Marble> marbles) {
        this.board = board;
        this.marbles = marbles;
    }

    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    public int getKillCount() {
        return killCount;
    }

    public void incrementKillCount() {
        killCount++;
    }
}
