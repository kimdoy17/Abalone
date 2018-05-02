package abalone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.Multimap;

import abalone.gameEnum.Direction;
import abalone.gameEnum.MarbleType;
import boardFrame.BoardEvaluation;
import boardFrame.GameFrame;

public class Search {
    
    private static final String ASCENDING_ORDER = "ASCENDING";
    private static final String DESCENDING_ORDER = "DESCENDING";

    private GameFrame frame;
    private MarbleType myType, oppType;
    public static int PRUNE = 0;

    public Search(GameFrame frame, MarbleType myType) {
        this.frame = frame;
        this.myType = myType;
        if(myType == MarbleType.WHITE)
            oppType = MarbleType.BLACK;
        else 
            oppType = MarbleType.WHITE;
        
    }

    public Cell[][] alphaBetaSearch(Board board, int depth) {

        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;
        Cell[][] finalCells = maxValue(board, min, max, depth);

        return finalCells;
    }

    public Cell[][] maxValue(Board board, int alpha, int beta, int depth) {

        Board maxBoard = null;

        int alphaMax = alpha;
        int betaMax = beta;

        if (depth == 0) {
            return board.returnAllCell();
        }
        int value = Integer.MIN_VALUE;

        depth--;
        for (Map.Entry<Integer, Board> treeMap : getEvaluations(board, DESCENDING_ORDER, myType, myType).entrySet()) {

            BoardEvaluation boardEvaluation = new BoardEvaluation(
                    new Board(minValue(treeMap.getValue(), alphaMax, betaMax, depth)));
            if (boardEvaluation.gameOver()) {
                return treeMap.getValue().returnAllCell();
            } else {
                int utility;
                if(myType == MarbleType.WHITE)
                    utility = boardEvaluation.getWhiteDifference();
                else
                    utility = boardEvaluation.getBlackDifference();
                
                if (utility > value) {
                    value = utility;
                    maxBoard = treeMap.getValue();
                }
                if (utility >= betaMax) {
                    //System.out.println("PRUNING" + PRUNE++);
                    return treeMap.getValue().returnAllCell();
                }
                if (utility > alphaMax) {
                    alphaMax = utility;
                }
            }
        }

        return maxBoard.returnAllCell();
    }


    public Cell[][] minValue(Board board, int alpha, int beta, int depth) {

        Board minBoard = null;

        int alphaMin = alpha;
        int betaMin = beta;

        if (depth == 0) {
            return board.returnAllCell();
        }
        int value = Integer.MAX_VALUE;
        depth--;
        for (Map.Entry<Integer, Board> treeMap : getEvaluations(board, ASCENDING_ORDER, oppType, myType).entrySet()) {

            BoardEvaluation boardEvaluation = new BoardEvaluation(new Board(maxValue(treeMap.getValue(), alphaMin, betaMin, depth)));

            if (boardEvaluation.gameOver()) {
                return treeMap.getValue().returnAllCell();
            } else {
                int utility;
                if(myType == MarbleType.WHITE)
                    utility = boardEvaluation.getWhiteDifference();
                else
                    utility = boardEvaluation.getBlackDifference();
                
                if (utility < value) {
                    value = utility;
                    minBoard = treeMap.getValue();
                }
                if (utility <= alphaMin) {
                    //System.out.println("PRUNING" + PRUNE++);
                    return treeMap.getValue().returnAllCell();
                }
                if (utility < betaMin) {
                    betaMin = utility;
                }
            }
        }
        return minBoard.returnAllCell();
    }

    public Cell[][] resultBoard(Pair<ArrayList<Marble>, Direction> currentPair, Cell[][] cells) {
        Board board = new Board(cells);
        int xMove = board.getMoveSets().get(currentPair.getValue()).x;
        int yMove = board.getMoveSets().get(currentPair.getValue()).y;
        for (Marble marble : currentPair.getKey()) {
            int x = marble.getCell().getX();
            int y = marble.getCell().getY();
            Marble m = board.getCellAt(x, y).getMarble();
            board.getCellAt(x, y).setMarble(null);
            board.getCellAt(x + xMove, y + yMove).setMarble(m);
            m.setCell(board.getCellAt(x + xMove, y + yMove));
        }
        return board.returnAllCell();
    }

    public TreeMap<Integer, Board> getEvaluations(Board board, String sortOrder, MarbleType type,
            MarbleType typeDifference) {

        TreeMap<Integer, Board> evaluations;

        if (sortOrder.equals("ASCENDING")) {
            evaluations = new TreeMap<Integer, Board>();
        } else if (sortOrder.equals("DESCENDING")) {
            evaluations = new TreeMap<Integer, Board>(Collections.reverseOrder());
        } else {
            evaluations = null;
        }

        Multimap<ArrayList<Marble>, Direction> a = board.allMoves_inMap(type);
        for (Entry<ArrayList<Marble>, Direction> entry : a.entries()) {
            Pair<ArrayList<Marble>, Direction> p = new Pair<>();
            p.put(entry.getKey(), entry.getValue());
            Board childBoard = new Board(resultBoard(p, board.returnAllCell()));
            BoardEvaluation boardBeingEvaluated = new BoardEvaluation(childBoard);
            if (typeDifference == MarbleType.BLACK) {
                evaluations.put(boardBeingEvaluated.getBlackDifference(), childBoard);
            } else if (typeDifference == MarbleType.WHITE) {
                evaluations.put(boardBeingEvaluated.getWhiteDifference(), childBoard);
            }
        }
        return evaluations;
    }

}
