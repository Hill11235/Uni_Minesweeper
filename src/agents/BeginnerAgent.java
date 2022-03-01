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

    private boolean sweepLoop(boolean verbose) {
        //probe clue cells?
        //scan all cells one by one
        //check for win, if won then return true
        //for each covered cell check adjacent cells
        //if cell has all free neighbours then probe
        //if cell has all marked neighbours then flag
        probeClues();

        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                Cell currentCell = new Cell(i, j);

                if (game.isWon()) {
                    return true;
                }

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

        return false;
    }

    private boolean allFreeNeighbours(Cell centreCell) {
        ArrayList<Cell> adjacentCells = centreCell.getAdjacentCells(agentBoard.length);
        for (Cell neighbour : adjacentCells) {
            char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];

            if (cellValue != 'b' && cellValue != '?' && cellValue != '*') {
                ArrayList<Cell> adjacentInception = neighbour.getAdjacentCells(agentBoard.length);
                int adjacentMines = Character.getNumericValue(cellValue);

                for (Cell secondAdjacent : adjacentInception) {
                    int numFlaggedNeighbours = getNumApplicableNeighbours(secondAdjacent, '*');

                    if (adjacentMines == numFlaggedNeighbours) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean allMarkedNeighbours(Cell centreCell) {
        ArrayList<Cell> adjacentCells = centreCell.getAdjacentCells(agentBoard.length);
        for (Cell neighbour : adjacentCells) {
            char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];

            if (cellValue != 'b' && cellValue != '?' && cellValue != '*') {
                ArrayList<Cell> adjacentInception = neighbour.getAdjacentCells(agentBoard.length);
                int adjacentMines = Character.getNumericValue(cellValue);

                for (Cell secondAdjacent : adjacentInception) {
                    int numCoveredNeighbours = getNumApplicableNeighbours(secondAdjacent, '?');
                    int numFlaggedNeighbours = getNumApplicableNeighbours(secondAdjacent, '*');

                    if (numCoveredNeighbours == adjacentMines - numFlaggedNeighbours) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private int getNumApplicableNeighbours(Cell centreCell, char match) {
        ArrayList<Cell> adjacentCells = centreCell.getAdjacentCells(agentBoard.length);
        int numApplicableNeighbours = 0;

        for (Cell neighbour: adjacentCells) {
            if (agentBoard[neighbour.getRow()][neighbour.getCol()] == match) {
                numApplicableNeighbours++;
            }
        }
        return numApplicableNeighbours;
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
