package support;

import java.util.Objects;

/**
 * Included as an idea to implement a cell class. May not use.
 */
public class Cell {

    private int row;
    private int col;
    private boolean blocked = false;
    private boolean flagged = false;
    private int adjacentBombs;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
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

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public int getAdjacentBombs() {
        return adjacentBombs;
    }

    public void setAdjacentBombs(int adjacentBombs) {
        this.adjacentBombs = adjacentBombs;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
}
