package agents;

import support.Cell;
import support.GameState;

import java.util.*;

/**
 * P4 class for using a SAT solver to solve problem using KB in CNF.
 */
public class CNFAgent extends DNFAgent {

    HashMap<Cell, Integer> cellNumberMap = new HashMap<>();
    Integer cellID = 1;
    ArrayList<int[]> KB = new ArrayList<>();

    public CNFAgent(GameState game) {
        super(game);
    }

    //  X run SPS
    //  X for each ?, get its numeric neighbour and generate CNF knowledge base
    //  X generate entailment KB
    //    convert entailment KB to DIMACS
    //    solve DIMACS output with SAT4J

    void generateCellMap() {
        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                Cell currentCell = new Cell(i, j);

                if (agentBoard[i][j] == '?') {
                    cellNumberMap.put(currentCell, cellID);
                    cellID++;
                }
            }
        }
    }

    @Override
    void clearKB() {
        this.KB = new ArrayList<>();
    }

    @Override
    public void sweep(boolean verbose) {
        boolean SpsResult = sps(verbose);
        if (SpsResult) {
            System.out.println("Final map\n");
            printAgentBoard();
            System.out.println("\nResult: Agent alive: all solved\n");
        } else {
            generateCellMap();
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
                List<Set<Integer>> dangerSubsets = getDangerSubsets(neighbour);
                generateSentence(dangerSubsets, neighbour, true);
                List<Set<Integer>> nonDangerSubsets = getNonDangerSubsets(neighbour);
                generateSentence(nonDangerSubsets, neighbour, false);
            }
        }
    }

    public void generateSentence(List<Set<Integer>> dangerSubset, Cell neighbour, boolean danger) {
        ArrayList<Cell> adjacentCovered = super.getApplicableNeighbours(neighbour, '?');

        for (Set subset: dangerSubset) {
            int[] option = generateOption(subset, adjacentCovered, danger);
            KB.add(option);
        }
    }

    private int[] generateOption(Set<Integer> subset, ArrayList<Cell> adjacentCells, boolean danger) {
        ArrayList<Integer> arrayBuilder = new ArrayList<>();

        for (Integer index : subset) {
            Cell currentCell = adjacentCells.get(index);
            int cellNum = cellNumberMap.get(currentCell);
            if (!danger) {
                cellNum = -cellNum;
            }
            arrayBuilder.add(cellNum);
        }

        return arrayBuilder.stream().mapToInt(Integer::intValue).toArray();
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
        //String satInput = KB + " & " + cellLetterMap.get(coveredCell).toString();
        return false;
    }

    private boolean entailsDanger(Cell coveredCell) {
        //String satInput = KB + " & ~" + cellLetterMap.get(coveredCell).toString();
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
        int numNonDangers = numAdjacentCovered - numAdjacentMines;

        if (numNonDangers == 1) {
            return getKCombinations(initialSet, initialSet.size());
        }

        return getKCombinations(initialSet, numNonDangers);
    }

    @Override
    public List<Set<Integer>> getDangerSubsets(Cell neighbour) {

        char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];
        int numAdjacentMines = Character.getNumericValue(cellValue);
        ArrayList<Cell> adjacentCovered = getApplicableNeighbours(neighbour, '?');
        int numAdjacentCovered = adjacentCovered.size();

        List<Integer> initialSet = getIntegerList(numAdjacentCovered);

        if (numAdjacentMines == 1) {
            return getKCombinations(initialSet, initialSet.size());
        }

        return getKCombinations(initialSet, numAdjacentMines);
    }

}
