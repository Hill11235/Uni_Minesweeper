package tests;

import agents.BasicAgent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.GameState;
import support.World;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BasicAgentTest {

    private GameState game1;
    private BasicAgent agent1;
    private GameState game2;
    private BasicAgent agent2;

    @BeforeEach
    public void setUp() {
        World world1 = World.TEST1;
        game1 = new GameState(world1);
        agent1 = new BasicAgent(game1);

        World world2 = World.TEST3;
        game2 = new GameState(world2);
        agent2 = new BasicAgent(game2);
    }

    //Failing as win condition isn't being triggered
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
                " 2| ? ? ? \n" +
                "\n" +
                "\n" +
                "Result: Agent alive: all solved" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    @Test
    void testSweepTEST1Verbose() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent1.sweep(true);
        String expectedOutput = "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| ? b b \n" +
                " 1| b ? b \n" +
                " 2| ? ? ? \n" +
                "\n" +
                "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 b b \n" +
                " 1| b 3 b \n" +
                " 2| ? ? ? \n" +
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

        agent2.sweep(false);
        String expectedOutput = "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 0 b \n" +
                " 1| 1 1 1 \n" +
                " 2| 1 - ? \n" +
                "\n" +
                "\n" +
                "Result: Agent dead: found mine" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }

    @Test
    void testSweepTEST3Verbose() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        agent2.sweep(true);
        String expectedOutput = "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| ? ? b \n" +
                " 1| ? ? ? \n" +
                " 2| ? ? ? \n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 0 b \n" +
                " 1| 1 1 1 \n" +
                " 2| ? ? ? \n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 0 b \n" +
                " 1| 1 1 1 \n" +
                " 2| 1 ? ? \n" +
                "\n" +
                "Final map\n" +
                "\n" +
                "\n" +
                "    0 1 2 \n" +
                "    - - - \n" +
                " 0| 0 0 b \n" +
                " 1| 1 1 1 \n" +
                " 2| 1 - ? \n" +
                "\n" +
                "\n" +
                "Result: Agent dead: found mine" +
                "\n" +
                "\n";
        assertEquals(outContent.toString(), expectedOutput);
        System.setOut(originalOut);
    }
}