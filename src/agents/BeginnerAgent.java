package agents;

import support.Cell;
import support.GameState;

import java.util.ArrayList;

/**
 * Uses single point strategy.
 */
public class BeginnerAgent extends BasicAgent {

    public BeginnerAgent(GameState game) {
        super(game);
    }

    @Override
    public void sweep(boolean verbose) {
        boolean result = sweepLoop(verbose);
        System.out.println("Final map\n");
        printAgentBoard();
        if (result) {
            System.out.println("\nResult: Agent alive: all solved\n");
        } else {
            System.out.println("\nResult: Agent not terminated\n");
        }
    }

    boolean sweepLoop(boolean verbose) {
        probeClues();

        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                Cell currentCell = new Cell(i, j);

                if (agentBoard[i][j] == '?') {
                    if (verbose) {
                        printAgentBoard();
                    }
                    if (allFreeNeighbours(currentCell)) {
                        probe(i, j);
                    } else if (allMarkedNeighbours(currentCell)) {
                        flag(i, j);
                    }
                }
            }
        }

        return game.isWon();
    }

    public boolean allFreeNeighbours(Cell centreCell) {
        ArrayList<Cell> adjacentCells = centreCell.getAdjacentCells(agentBoard.length);
        for (Cell neighbour : adjacentCells) {
            char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];

            if (cellValue != 'b' && cellValue != '?' && cellValue != '*') {
                int adjacentMines = Character.getNumericValue(cellValue);
                int numFlaggedNeighbours = getNumApplicableNeighbours(neighbour, '*');
                if (adjacentMines == numFlaggedNeighbours) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean allMarkedNeighbours(Cell centreCell) {
        ArrayList<Cell> adjacentCells = centreCell.getAdjacentCells(agentBoard.length);
        for (Cell neighbour : adjacentCells) {
            char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];

            if (cellValue != 'b' && cellValue != '?' && cellValue != '*') {
                int adjacentMines = Character.getNumericValue(cellValue);
                int numCoveredNeighbours = getNumApplicableNeighbours(neighbour, '?');
                int numFlaggedNeighbours = getNumApplicableNeighbours(neighbour, '*');

                if (numCoveredNeighbours == adjacentMines - numFlaggedNeighbours) {
                    return true;
                }
            }
        }

        return false;
    }

    public int getNumApplicableNeighbours(Cell centreCell, char match) {
        ArrayList<Cell> adjacentCells = centreCell.getAdjacentCells(agentBoard.length);
        int numApplicableNeighbours = 0;

        for (Cell neighbour: adjacentCells) {
            if (agentBoard[neighbour.getRow()][neighbour.getCol()] == match) {
                numApplicableNeighbours++;
            }
        }
        return numApplicableNeighbours;
    }

    //TODO add test
    public ArrayList<Cell> getApplicableNeighbours(Cell centreCell, char match) {
        ArrayList<Cell> adjacentCells = centreCell.getAdjacentCells(agentBoard.length);
        ArrayList<Cell> applicableNeighbours = new ArrayList<>();

        for (Cell neighbour: adjacentCells) {
            if (agentBoard[neighbour.getRow()][neighbour.getCol()] == match) {
                applicableNeighbours.add(neighbour);
            }
        }

        return applicableNeighbours;
    }

    private void probeClues() {
        int midPoint = agentBoard.length / 2;
        probe(0, 0);
        probe(midPoint, midPoint);
    }

    @Override
    public void flag(int row, int col) {
        agentBoard[row][col] = '*';
    }
}
