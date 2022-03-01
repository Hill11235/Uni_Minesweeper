package tests;

import org.junit.jupiter.api.Test;
import support.Cell;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    Cell testCell1 = new Cell(1, 1);
    Cell testCell2 = new Cell(5, 6);
    Cell testCell3 = new Cell(6, 6);

    @Test
    public void testGetAdjacentCells() {
        ArrayList<Cell> adjacentCells1 = testCell1.getAdjacentCells(3);
        String adjacentString1 = "[[0,0], [0,1], [0,2], [1,0], [1,2], [2,0], [2,1], [2,2]]";
        assertEquals(adjacentCells1.toString(), adjacentString1);

        ArrayList<Cell> adjacentCells2 = testCell2.getAdjacentCells(7);
        String adjacentString2 = "[[4,5], [4,6], [5,5], [6,5], [6,6]]";
        assertEquals(adjacentCells2.toString(), adjacentString2);

        ArrayList<Cell> adjacentCells3 = testCell3.getAdjacentCells(7);
        String adjacentString3 = "[[5,5], [5,6], [6,5]]";
        assertEquals(adjacentCells3.toString(), adjacentString3);
    }

    @Test
    void testEquals() {
        Cell a = new Cell(2, 2);
        Cell b = new Cell(2, 2);
        assertEquals(a, b);
    }

    @Test
    void testHashCode() {
        Cell a = new Cell(2, 2);
        Cell b = new Cell(2, 2);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testGetRow() {
        Cell a = new Cell(2, 3);
        assertEquals(a.getRow(), 2);
    }

    @Test
    void testGetCol() {
        Cell a = new Cell(2, 3);
        assertEquals(a.getCol(), 3);
    }

    @Test
    void testToString() {
        Cell a = new Cell(2, 3);
        assertEquals(a.toString(), "[2,3]");
    }
}