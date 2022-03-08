package support;

import java.util.ArrayList;

/**
 * Class for game logic that the agents can query.
 */
public class GameState {

    private final char[][] gameBoard;
    private int numMines;
    private final int numCells;
    private final ArrayList<Cell> blockedCells = new ArrayList<>();
    private final ArrayList<Cell> probedCells = new ArrayList<>();
    private boolean lost;

    /**
     * Constructor, takes world as arg.
     * @param world one of the defined game worlds.
     */
    public GameState(World world) {
        this.gameBoard = world.map;
        this.numCells = gameBoard.length * gameBoard.length;
        setBlockedCells();
        setNumMines();
    }

    /**
     * Loops through all positions in the game and returns the number of mine cells.
     */
    private void setNumMines() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                if (gameBoard[i][j] == 'm') {
                    numMines++;
                }
            }
        }
    }

    /**
     * Loops through all positions in the game and adds of the blocked cells to a list.
     */
    private void setBlockedCells() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                if (gameBoard[i][j] == 'b') {
                    Cell blockedCell = new Cell(i, j);
                    blockedCells.add(blockedCell);
                }
            }
        }
    }

    /**
     * Generates an initial view of the game for the agent.
     * Only includes blocked cells and with all other cells covered.
     * @return concealed version of world for agent.
     */
    public char[][] generateAgentBoard() {
        int boardSize = gameBoard.length;
        char[][] agentBoard = new char[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (gameBoard[i][j] == 'b') {
                    agentBoard[i][j] = 'b';
                } else {
                    agentBoard[i][j] = '?';
                }
            }
        }
        return agentBoard;
    }

    /**
     * This is the method the agents call when they are probing.
     * When a cell is probed it gets added to the list of probed cells.
     * If the probed cell is a mine, then the game is marked as lost.
     * @param cell cell to checked.
     * @return what is in the cell to the agent.
     */
    public char getCell(Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        char cellValue = gameBoard[row][col];

        if (cellValue == 'm') {
            this.lost = true;
        }

        if (!probedCells.contains(cell)) {
            probedCells.add(cell);
        }

        return cellValue;
    }

    /**
     * Checks if game is won.
     * Per rules game won when the number of probed cells is equal to the total number of cells minus the number of mines.
     * @return true if won.
     */
    public boolean isWon() {
        return probedCells.size() == (numCells - numMines - blockedCells.size());
    }

    /**
     * Checks if game lost. Game lost if mine probed.
     * @return true if lost.
     */
    public boolean isLost() {
        return lost;
    }

    /**
     * Returns the number of mines in the world.
     * @return number of mines.
     */
    public int getNumMines() {
        return this.numMines;
    }
}
