package support;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Cell class used to represent each individual cell in a board.
 */
public class Cell {

    private final int row;
    private final int col;

    /**
     * Constructor, initialises the coordinates.
     * @param row row position.
     * @param col column position.
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * For a given cell in a board of width boardSize, get an ArrayList of the surrounding cells.
     * Checks that the Cells returned would not be outside the boundaries of the world.
     * @param boardSize width of the game world.
     * @return list of surrounding cells.
     */
    public ArrayList<Cell> getAdjacentCells(int boardSize) {
        ArrayList<Cell> cellList = new ArrayList<>();
        int rowLowerBound = Math.max(0, row - 1);
        int rowUpperBound = Math.min(boardSize, row + 2);
        int colLowerBound = Math.max(0, col - 1);
        int colUpperBound = Math.min(boardSize, col + 2);

        for (int i = rowLowerBound; i < rowUpperBound; i++) {
            for (int j = colLowerBound; j < colUpperBound; j++) {
                Cell adjacentCell = new Cell(i, j);
                cellList.add(adjacentCell);
            }
        }

        cellList.remove(this);

        return cellList;
    }

    /**
     * Checks that two cells are equal by comparing their coordinates.
     * @param o Cell object.
     * @return true if same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    /**
     * Hashes the cell based on its row and column position.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Get row position.
     * @return row position of cell.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Get column position.
     * @return column position of cell.
     */
    public int getCol() {
        return this.col;
    }

    /**
     * When Cell is needed as a String, present its coordinate position.
     * @return coordinate position as String.
     */
    public String toString() {
        return "[" + this.row + "," + this.col + "]";
    }
}
