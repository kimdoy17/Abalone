package abalone;

import abalone.gameEnum.MarbleType;
import boardFrame.GameFrame;

public class Agent {

    private final int depth = 3;
    private GameFrame frame;
    private MarbleType agentType;
    public Agent(GameFrame frame, MarbleType type) {
        this.frame = frame;
        this.agentType = type;

    }

    public void agentMove() {

        Search searchObject = new Search(frame, agentType);
        Board tempBoard = new Board(frame.getBoard().returnAllCell());

        Cell[][] c = searchObject.alphaBetaSearch(tempBoard, depth);

        frame.getBoard().replaceCells(c);

        frame.getBoard().clearMarbles();
        frame.getBoard().setNumOfMove();
        frame.updateTotalTime();

        Board.PLAYER_TURN = Board.OPPONENT_MAP.get(Board.PLAYER_TURN);
        GameFrame.turnOver = true;


    }

    public MarbleType getType() {
        return agentType;
    }
}