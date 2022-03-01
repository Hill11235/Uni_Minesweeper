package support;

import support.Cell;

import java.util.ArrayList;

public class GameState {

    private char[][] gameBoard;
    private int numMines;
    private int numCells;
    private ArrayList<Cell> blockedCells = new ArrayList<>();
    private ArrayList<Cell> probedCells = new ArrayList<>();
    private boolean lost;

    public GameState(World world) {
        this.gameBoard = world.map;
        this.numCells = gameBoard.length * gameBoard.length;
        setBlockedCells();
        setNumMines();
    }

    private void setNumMines() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                if (gameBoard[i][j] == 'm') {
                    numMines++;
                }
            }
        }
    }

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

    public boolean isWon() {
        //System.out.println("Probed size: " + probedCells.size());
        //System.out.println("Win size: " + (numCells - numMines - blockedCells.size()));
        return probedCells.size() == (numCells - numMines - blockedCells.size());
    }

    public boolean isLost() {
        return lost;
    }

    public int getNumMines() {
        return this.numMines;
    }
}
