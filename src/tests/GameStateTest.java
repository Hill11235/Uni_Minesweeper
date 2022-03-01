package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.GameState;
import support.World;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    GameState game;

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

    @Test
    void getCell() {
    }

    @Test
    void isWon() {
    }

    @Test
    void isLost() {
    }

    @Test
    void getNumMines() {
    }
}