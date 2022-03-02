package tests;

import agents.BeginnerAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.Cell;
import support.GameState;
import support.World;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class BeginnerAgentTest {

    private GameState game1;
    private BeginnerAgent agent1;
    private GameState game2;
    private BeginnerAgent agent2;
    private GameState game3;
    private BeginnerAgent agent3;

    @BeforeEach
    public void setUp() {
        World world1 = World.TEST1;
        game1 = new GameState(world1);
        agent1 = new BeginnerAgent(game1);

        World world2 = World.TEST2;
        game2 = new GameState(world2);
        agent2 = new BeginnerAgent(game2);

        World world3 = World.TEST3;
        game3 = new GameState(world3);
        agent3 = new BeginnerAgent(game3);
    }

    @Test
    void testSweepTEST1() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent1.sweep(false);
        String expectedOutput = "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 b b \n" +
                " 1| b 3 b \n" +
                " 2| * * * \n" +
                "\n" +
                "\n" +
                "Result: Agent alive: all solved" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    @Test
    void testSweepTEST2() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent2.sweep(false);
        String expectedOutput = "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 2 * * \n" +
                " 1| * 5 * \n" +
                " 2| b b * \n" +
                "\n" +
                "\n" +
                "Result: Agent alive: all solved" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    @Test
    void testSweepTEST3() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent3.sweep(false);
        String expectedOutput = "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 0 b \n" +
                " 1| 1 1 1 \n" +
                " 2| ? ? ? \n" +
                "\n" +
                "\n" +
                "Result: Agent not terminated" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    /**
     * Check instance of AFN which should return false.
     */
    @Test
    public void testAllFreeNeighboursFailing() {
        Cell centreCell = new Cell(1, 1);
        assertFalse(agent1.allFreeNeighbours(centreCell));
    }

    /**
     * Check instance of AMN which should return false.
     */
    @Test
    public void testAllMarkedNeighboursFailing() {
        Cell centreCell = new Cell(1, 1);
        assertFalse(agent1.allMarkedNeighbours(centreCell));
    }

    /**
     * Checks that the correct number of matched surrounding cells is returned correctly.
     */
    @Test
    public void testGetApplicableNeighbours() {
        Cell centreCell = new Cell(1, 1);
        int blockedCells = agent1.getNumApplicableNeighbours(centreCell, 'b');
        assertEquals(blockedCells, 4);
    }
}
