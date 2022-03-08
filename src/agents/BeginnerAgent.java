package agents;

import support.Cell;
import support.GameState;

import java.util.ArrayList;

/**
 * Uses single point strategy.
 */
public class BeginnerAgent extends BasicAgent {

    /**
     * Constructor.
     * @param game game infrastructure for agent.
     */
    public BeginnerAgent(GameState game) {
        super(game);
    }

    /**
     * Sweeps through board using solves using agent specific strategy.
     */
    @Override
    public void sweep(boolean verbose) {
        boolean result = sps(verbose);
        System.out.println("Final map\n");
        printAgentBoard();
        if (result) {
            System.out.println("\nResult: Agent alive: all solved\n");
        } else {
            System.out.println("\nResult: Agent not terminated\n");
        }
    }

    /**
     * Single point strategy.
     * Loops through all cells and checks all free neighbours and all marked neighbours on each covered cell.
     * @return true if game is won.
     */
    public boolean sps(boolean verbose) {
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

    /**
     * For a given numeric Cell checks if all adjacent mines have been flagged.
     * @param centreCell check neighbours of this Cell.
     * @return true if all free neighbours.
     */
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

    /**
     * For a given numeric cell checks if all the number of adjacent covered cells is equal to its value.
     * @param centreCell check neighbours of this cell.
     * @return true if all marked neighbours.
     */
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

    /**
     * Get the number of adjacent cells of the requested type.
     * @param centreCell check neighbours of this cell.
     * @param match char to be checked for.
     * @return number of matches
     */
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

    /**
     * Get the adjacent cells of the requested type.
     * @param centreCell check neighbours of this cell.
     * @param match char to be checked for.
     * @return list of matched cells.
     */
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

    /**
     * Per the rules probe the top left cell and central cell.
     */
    private void probeClues() {
        int midPoint = agentBoard.length / 2;
        probe(0, 0);
        probe(midPoint, midPoint);
    }

    /**
     * Flag the specified position.
     * @param row row coordinate.
     * @param col col coordinate.
     */
    @Override
    public void flag(int row, int col) {
        agentBoard[row][col] = '*';
    }
}
