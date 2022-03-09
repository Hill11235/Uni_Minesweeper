import agents.BasicAgent;
import agents.BeginnerAgent;
import agents.CNFAgent;
import agents.DNFAgent;
import support.GameState;
import support.World;

public class A2main {

    public static void main(String[] args) {

        boolean verbose=false;
        if (args.length>2 && args[2].equals("verbose") ){
            verbose=true; //prints agent's view at each step if true
        }

        System.out.println("-------------------------------------------\n");
        System.out.println("Agent " + args[0] + " plays " + args[1] + "\n");

        World world = World.valueOf(args[1]);

        char[][] board = world.map;
        printBoard(board);
        System.out.println("Start!");

        switch (args[0]) {
        case "P1":
            GameState game1 = new GameState(world);
            BasicAgent basic = new BasicAgent(game1);
            basic.sweep(verbose);
            break;
        case "P2":
            GameState game2 = new GameState(world);
            BeginnerAgent beginner = new BeginnerAgent(game2);
            beginner.sweep(verbose);
            break;
        case "P3":
            GameState game3 = new GameState(world);
            DNFAgent dnf = new DNFAgent(game3);
            dnf.sweep(verbose);
            break;
        case "P4":
            GameState game4 = new GameState(world);
            CNFAgent cnf = new CNFAgent(game4);
            cnf.sweep(verbose);
            break;
        case "P5":
            //TODO: Part 5
            break;
        }

        //templates to print results - copy to appropriate places
        //System.out.println("\nResult: Agent alive: all solved\n");
        //System.out.println("\nResult: Agent dead: found mine\n");
        //System.out.println("\nResult: Agent not terminated\n");
    }

    //prints the board in the required format - PLEASE DO NOT MODIFY
    public static void printBoard(char[][] board) {
        System.out.println();
        // first line
        System.out.print("    ");
        for (int j = 0; j < board[0].length; j++) {
            System.out.print(j + " "); // x indexes
        }
        System.out.println();
        // second line
        System.out.print("    ");
        for (int j = 0; j < board[0].length; j++) {
            System.out.print("- ");// separator
        }
        System.out.println();
        // the board
        for (int i = 0; i < board.length; i++) {
            System.out.print(" "+ i + "| ");// index+separator
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");// value in the board
            }
            System.out.println();
        }
        System.out.println();
    }
}
