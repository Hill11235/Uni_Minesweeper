package agents;

import org.logicng.io.parsers.ParserException;
import support.Cell;
import support.GameState;

/**
 * P4 class for using a SAT solver to solve problem using KB in CNF.
 */
public class CNFAgent extends DNFAgent {

    public CNFAgent(GameState game) {
        super(game);
    }

    //  X run SPS
    //    for each ?, get its numeric neighbour and generate CNF knowledge base
    //    generate entailment KB
    //    convert entailment KB to DIMACS
    //    solve DIMACS output with SAT4J

    @Override
    public void sweep(boolean verbose) {
        boolean SpsResult = sps(verbose);
        if (SpsResult) {
            System.out.println("Final map\n");
            printAgentBoard();
            System.out.println("\nResult: Agent alive: all solved\n");
        } else {
            generateCellLetterMap();
            for (int i = 0; i < agentBoard.length; i++) {
                solve(verbose);
            }
            System.out.println("Final map\n");
            printAgentBoard();

            if (!game.isWon()) {
                System.out.println("\nResult: Agent not terminated\n");
            } else if (game.isWon()) {
                System.out.println("\nResult: Agent alive: all solved\n");
            }
        }
    }

    @Override
    public void solve(boolean verbose) {

    }

    private void generateKB() {

    }

    @Override
    void SATSweep(boolean verbose) {
        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                Cell currentCell = new Cell(i, j);

                if (agentBoard[i][j] == '?') {
                    if (verbose) {
                        printAgentBoard();
                    }
                    entailmentChecks(currentCell);
                }
            }
        }
    }

    private void entailmentChecks(Cell currentCell) {
        if (!entailsNoDanger(currentCell)) {
            probe(currentCell.getRow(), currentCell.getCol());
        } else if (!entailsDanger(currentCell)) {
            flag(currentCell.getRow(), currentCell.getCol());
        }
    }

    private boolean entailsNoDanger(Cell coveredCell) {
        return false;
    }

    private boolean entailsDanger(Cell coveredCell) {
        return false;
    }


}
