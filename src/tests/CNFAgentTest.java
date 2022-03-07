package tests;

import agents.CNFAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.GameState;
import support.World;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CNFAgentTest {

    private GameState game3;
    private CNFAgent agent3;
    private GameState game4;
    private CNFAgent agent4;
    private GameState game5;
    private CNFAgent agent5;
    private GameState game6;
    private CNFAgent agent6;
    private GameState game7;
    private CNFAgent agent7;

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

        World world7 = World.TEST7;
        game7 = new GameState(world7);
        agent7 = new CNFAgent(game7);
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
    void testSweepTEST7() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent7.sweep(false);
        String expectedOutput = "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 b ? \n" +
                " 1| b 4 ? \n" +
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
    void testSMALL6() {
        World world = World.SMALL6;
        GameState game = new GameState(world);
        CNFAgent agent = new CNFAgent(game);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent.sweep(false);
        String expectedOutput = "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 3 4 \n" +
                "    - - - - - \n" +
                " 0| 0 0 0 0 b \n" +
                " 1| 1 1 0 0 0 \n" +
                " 2| * 2 1 b 0 \n" +
                " 3| 4 * 2 0 b \n" +
                " 4| * * 2 0 0 \n" +
                "\n" +
                "\n" +
                "Result: Agent alive: all solved" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    @Test
    void runSMALL7() {
        World world = World.SMALL7;
        GameState game = new GameState(world);
        CNFAgent agent = new CNFAgent(game);
        agent.sweep(true);
    }

    /**
     * Run all worlds and ensure there are no crashes.
     */
    @Test
    void runAllWorlds() {
        for (World world: World.values()) {
            GameState game = new GameState(world);
            CNFAgent agent = new CNFAgent(game);
            System.out.println(world.name());
            agent.sweep(false);
        }
    }
}
