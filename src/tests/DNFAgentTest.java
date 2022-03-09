package tests;

import agents.DNFAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.GameState;
import support.World;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for logic and methods in DNFAgent.
 */
public class DNFAgentTest {

    private GameState game3;
    private DNFAgent agent3;
    private GameState game4;
    private DNFAgent agent4;
    private GameState game5;
    private DNFAgent agent5;
    private GameState game6;
    private DNFAgent agent6;
    private GameState game7;
    private DNFAgent agent7;

    /**
     * Set up agents and worlds before each test.
     */
    @BeforeEach
    public void setUp() {
        World world3 = World.TEST3;
        game3 = new GameState(world3);
        agent3 = new DNFAgent(game3);

        World world4 = World.TEST4;
        game4 = new GameState(world4);
        agent4 = new DNFAgent(game4);

        World world5 = World.TEST5;
        game5 = new GameState(world5);
        agent5 = new DNFAgent(game5);

        World world6 = World.TEST6;
        game6 = new GameState(world6);
        agent6 = new DNFAgent(game6);

        World world7 = World.TEST7;
        game7 = new GameState(world7);
        agent7 = new DNFAgent(game7);
    }

    /**
     * Tests for the correct answer and stacscheck format.
     */
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

    /**
     * Tests for the correct answer and stacscheck format.
     */
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

    /**
     * Tests for the correct answer and stacscheck format.
     */
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

    /**
     * Tests for the correct answer and stacscheck format.
     */
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

    /**
     * Tests for the correct answer and stacscheck format.
     */
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

    /**
     * Tests that the correct combinations are returned.
     */
    @Test
    public void testGetKCombinations() {
        ArrayList<Integer> masterSet = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            masterSet.add(i);
        }

        //test size correct
        assertEquals(6, agent3.getKCombinations(masterSet, 2).size());
        assertEquals(4, agent3.getKCombinations(masterSet, 1).size());

        //test contents correct
        String output4C2 = "[[0, 1], [0, 2], [0, 3], [1, 2], [1, 3], [2, 3]]";
        List<Set<Integer>> listCombo = agent3.getKCombinations(masterSet, 2);
        assertEquals(output4C2, listCombo.toString());
    }

    /**
     * Test game SMALL6 as this was used for debugging purposes.
     */
    @Test
    void testSMALL6() {
        World world = World.SMALL6;
        GameState game = new GameState(world);
        DNFAgent agent = new DNFAgent(game);
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

    /**
     * Test game SMALL7 as this was used for debugging purposes.
     */
    @Test
    void testSMALL7() {
        World world = World.SMALL7;
        GameState game = new GameState(world);
        DNFAgent agent = new DNFAgent(game);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent.sweep(false);
        String expectedOutput = "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 3 4 \n" +
                "    - - - - - \n" +
                " 0| 1 * 2 * b \n" +
                " 1| 1 b 2 2 2 \n" +
                " 2| 0 b 0 1 * \n" +
                " 3| 1 1 0 b 1 \n" +
                " 4| * 1 0 0 0 \n"+
                "\n" +
                "\n" +
                "Result: Agent alive: all solved" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    /**
     * Run all available worlds and ensure that no mines are probed.
     */
    @Test
    void checkNoMinesProbed() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        for (World world: World.values()) {
            GameState game = new GameState(world);
            DNFAgent agent = new DNFAgent(game);
            agent.sweep(false);
        }

        String allOutputs = outContent.toString();
        String cleanedOutputs = allOutputs.replaceAll("    (- )+", " ");
        assertFalse(cleanedOutputs.contains("-"));
        System.setOut(originalOut);
    }
}
