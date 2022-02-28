import java.util.ArrayList;

/**
 * As per lectures, this implements the player's role.
 */
public class BasicAgent {

    private ArrayList<Cell> unprobed = new ArrayList<>();
    private ArrayList<Cell> probed = new ArrayList<>();;
    private char[][] agentBoard;
    private int numMines;
    private int boardSize;

    public BasicAgent(char[][] agentBoard, int numMines) {
        this.agentBoard = agentBoard;
        this.numMines = numMines;
        this.boardSize = agentBoard.length;
    }

    //TODO implement part 1 sweep
    public void sweep() {

    }


    //TODO implement cell probe and what the function should do in each return scenario
    public void probe(int row, int col) {

    }

    /**
     * Override in subclasses for advanced tasks.
     * @param row row coordinate.
     * @param col col coordinate.
     */
    public void flag(int row, int col) {

    }

    public void printFinalBoard() {

    }
}
