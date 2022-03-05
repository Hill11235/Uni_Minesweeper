package tests;

import agents.CNFAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.GameState;
import support.World;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class CNFAgentTest {

    private GameState game3;
    private CNFAgent agent3;
    private GameState game4;
    private CNFAgent agent4;
    private GameState game5;
    private CNFAgent agent5;
    private GameState game6;
    private CNFAgent agent6;

    @BeforeEach
    public void setUp() {
        World world3 = World.TEST3;
        game3 = new GameState(world3);
        agent3 = new CNFAgent(game3);

        World world4 = World.TEST4;
        game4 = new GameState(world4);
        agent4 = new CNFAgent(game4);

        World world5 = World.TEST5;
        game5 = new GameState(world5);
        agent5 = new CNFAgent(game5);

        World world6 = World.TEST6;
        game6 = new GameState(world6);
        agent6 = new CNFAgent(game6);
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
                " 2| 1 * 1 \n" +
                "\n" +
                "\n" +
                "Result: Agent alive: all solved" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    @Test
    void testSweepTEST4() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent4.sweep(false);
        String expectedOutput = "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 1 * \n" +
                " 1| 0 2 2 \n" +
                " 2| b 1 * \n" +
                "\n" +
                "\n" +
                "Result: Agent alive: all solved" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    @Test
    void testSweepTEST5() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent5.sweep(false);
        String expectedOutput = "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 1 * \n" +
                " 1| 0 1 1 \n" +
                " 2| 0 b 0 \n" +
                "\n" +
                "\n" +
                "Result: Agent alive: all solved" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    @Test
    void testSweepTEST6() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent6.sweep(false);
        String expectedOutput = "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 b ? \n" +
                " 1| b 2 ? \n" +
                " 2| ? ? ? \n" +
                "\n" +
                "\n" +
                "Result: Agent not terminated" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    @Test
    public void testVerbose() {
        agent6.sweep(true);
    }
}
