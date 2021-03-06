package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.Cell;
import support.GameState;
import support.World;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test that the logic in GameState behaves as anticipated.
 */
class GameStateTest {

    private GameState game;

    /**
     * Initialise fresh game before each test.
     */
    @BeforeEach
    public void setUp() {
        World world = World.TEST1;
        game = new GameState(world);
    }

    /**
     * Check that a board with '?' is generated for all non-blocked cells.
     */
    @Test
    void generateAgentBoard() {
        char[][] generatedBoard = new char[][] {{'?', 'b', 'b'}, {'b', '?', 'b'}, {'?', '?', '?'}};

        for (int i = 0; i < generatedBoard.length; i++) {
            for (int j = 0; j < generatedBoard.length; j++) {
                assertEquals(generatedBoard[i][j], game.generateAgentBoard()[i][j]);
            }
        }
    }

    /**
     * Get zero cell and check return.
     */
    @Test
    void getZeroCell() {
        Cell safeCell = new Cell(0, 0);
        char check = game.getCell(safeCell);
        assertEquals(check, '0');
    }

    /**
     * Get a mine cell and then check that the game is lost.
     */
    @Test
    void getMineCell() {
        Cell mineCell = new Cell(2, 0);
        char check = game.getCell(mineCell);
        assertEquals(check, 'm');
        assertTrue(game.isLost());
    }

    /**
     * Test get method on all non-mine cells and then check game is won.
     */
    @Test
    void getAllNonMineCells() {
        char[][] board = game.generateAgentBoard();
        for (int i = 0; i < board.length - 1; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != 'b') {
                    Cell currentCell = new Cell(i, j);
                    game.getCell(currentCell);
                }
            }
        }
        assertTrue(game.isWon());
    }

    /**
     * Check at the beginning of the game that won is false.
     */
    @Test
    void isWon() {
        assertFalse(game.isWon());
    }

    /**
     * Check at the beginning of the game that lost is false.
     */
    @Test
    void isLost() {
        assertFalse(game.isLost());
    }

    /**
     * Check that the number of mines is correct.
     */
    @Test
    void getNumMines() {
        assertEquals(game.getNumMines(), 3);
    }
}