package support;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Included as an idea to implement a cell class. May not use.
 */
public class Cell {

    private int row;
    private int col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public String toString() {
        return "[" + this.row + "," + this.col + "]";
    }
}
