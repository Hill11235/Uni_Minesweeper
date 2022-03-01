package agents;

import support.A2main;
import support.Cell;
import support.GameState;

import java.util.ArrayList;

/**
 * As per lectures, this implements the player's role.
 */
public class BasicAgent {

    private GameState game;
    private ArrayList<Cell> unprobed = new ArrayList<>();
    private ArrayList<Cell> probed = new ArrayList<>();
    private char[][] agentBoard;
    private int numMines;

    public BasicAgent(GameState game) {
        this.game = game;
        this.agentBoard = game.generateAgentBoard();
        this.numMines = game.getNumMines();
    }


    public void sweep() {
        boolean result = sweepLoop();
        printFinalBoard();
        if (result) {
            System.out.println("\nResult: Agent alive: all solved\n");
        } else {
            System.out.println("\nResult: Agent dead: found mine\n");
        }
    }

    private boolean sweepLoop() {
        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                probe(i, j);
                if (game.isLost()) {
                    return false;
                } else if (game.isWon()) {
                    return true;
                }
            }
        }
        return true;
    }

    private void probe(int row, int col) {
        char probedCell = game.getCell(row, col);
        probed.add(new Cell(row, col));
        if (probedCell == 'm') {
            probedCell = '-';
        }
        agentBoard[row][col] = probedCell;
    }

    /**
     * Override in subclasses for advanced tasks.
     * @param row row coordinate.
     * @param col col coordinate.
     */
    public void flag(int row, int col) {

    }

    private void printFinalBoard() {
        A2main.printBoard(agentBoard);
    }
}
