package agents;

import support.Cell;
import support.GameState;

import java.util.ArrayList;

/**
 * As per lectures, this implements the player's role.
 */
public class BasicAgent {

    GameState game;
    private ArrayList<Cell> blockedCells = new ArrayList<>();
    private ArrayList<Cell> probedCells = new ArrayList<>();
    char[][] agentBoard;
    private int numMines;

    /**
     * Constructor, takes GameState arg.
     * @param game linked game infrastructure.
     */
    public BasicAgent(GameState game) {
        this.game = game;
        this.agentBoard = game.generateAgentBoard();
        this.numMines = game.getNumMines();
        initialiseBlockedList();
    }

    /**
     * Sweeps through board using basic logic and prints result.
     */
    public void sweep(boolean verbose) {
        boolean result = sweepLoop(verbose);
        System.out.println("Final map\n");
        printAgentBoard();
        if (result) {
            System.out.println("\nResult: Agent alive: all solved\n");
        } else {
            System.out.println("\nResult: Agent dead: found mine\n");
        }
    }

    /**
     * Loops through all cells and returns true for victory and false for failure.
     * @return boolean indicating result.
     */
    private boolean sweepLoop(boolean verbose) {

        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                if (agentBoard[i][j] == '?') {
                    if (verbose) {
                        printAgentBoard();
                    }
                    probe(i, j);
                }
                if (game.isLost()) {
                    return false;
                } else if (game.isWon()) {
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * Probes a given cell using the provided coordinates.
     * @param row row coordinate.
     * @param col column coordinate.
     */
    void probe(int row, int col) {
        Cell probedCell = new Cell(row, col);

        if (!probedCells.contains(probedCell) && !blockedCells.contains(probedCell)) {
            char probedChar = game.getCell(probedCell);
            probedCells.add(probedCell);

            if (probedChar == 'm') {
                probedChar = '-';
            } else if (probedChar == '0') {
                ArrayList<Cell> adjacentCells = probedCell.getAdjacentCells(agentBoard.length);
                for (Cell c : adjacentCells) {
                    if (!probedCells.contains(c)) {
                        probe(c.getRow(), c.getCol());
                    }
                }
            }
            agentBoard[row][col] = probedChar;
        }
    }

    /**
     * Override in subclasses for advanced tasks.
     * @param row row coordinate.
     * @param col col coordinate.
     */
    public void flag(int row, int col) {}

    private void initialiseBlockedList() {
        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                if (agentBoard[i][j] == 'b') {
                    Cell blockedCell = new Cell(i, j);
                    blockedCells.add(blockedCell);
                }
            }
        }
    }

    /**
     * Print board at end of the game.
     */
    public void printAgentBoard() {
        System.out.println();
        // first line
        System.out.print("    ");
        for (int j = 0; j < agentBoard[0].length; j++) {
            System.out.print(j + " "); // x indexes
        }
        System.out.println();
        // second line
        System.out.print("    ");
        for (int j = 0; j < agentBoard[0].length; j++) {
            System.out.print("- ");// separator
        }
        System.out.println();
        // the board
        for (int i = 0; i < agentBoard.length; i++) {
            System.out.print(" "+ i + "| ");// index+separator
            for (int j = 0; j < agentBoard[0].length; j++) {
                System.out.print(agentBoard[i][j] + " ");// value in the board
            }
            System.out.println();
        }
        System.out.println();
    }
}
