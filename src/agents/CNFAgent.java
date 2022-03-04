package agents;

import support.Cell;
import support.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        generateKB();
    }

    @Override
    public void createSentence(Cell currentCell) {
        ArrayList<Cell> adjacentCells = currentCell.getAdjacentCells(agentBoard.length);

        for (Cell neighbour : adjacentCells) {
            char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];

            if (cellValue != 'b' && cellValue != '?' && cellValue != '*') {
                //get danger subsets
                //generate at most N danger sentence using danger subsets
                //get non-danger subsets
                //generate at most N non-danger sentence using non-danger subsets
                //combine these sentences using AND and add to KB
            }
        }
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

    /**
     * Used to generate non-danger combinations given a cell.
     * @param neighbour
     * @return
     */
    public List<Set<Integer>> getNonDangerSubsets(Cell neighbour) {
        char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];
        int numAdjacentMines = Character.getNumericValue(cellValue);
        ArrayList<Cell> adjacentCovered = getApplicableNeighbours(neighbour, '?');
        int numAdjacentCovered = adjacentCovered.size();

        List<Integer> initialSet = getIntegerList(numAdjacentCovered);
        int numNonDangers = cellValue - numAdjacentMines;

        return getKCombinations(initialSet, numNonDangers);
    }

}
