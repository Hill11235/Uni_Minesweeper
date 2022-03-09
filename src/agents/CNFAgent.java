package agents;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import support.Cell;
import support.GameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * P4 class for using a SAT solver to solve problem using KB in CNF.
 */
public class CNFAgent extends DNFAgent {

    HashMap<Cell, Integer> cellNumberMap = new HashMap<>();
    Integer cellID = 1;
    ArrayList<int[]> KB = new ArrayList<>();

    /**
     * Constructor for class.
     * @param game linked game logic.
     */
    public CNFAgent(GameState game) {
        super(game);
    }

    /**
     * Maps each cell in the game to an integer.
     * Used to build DIMACS.
     */
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

    /**
     * Clear the knowledge base between changes.
     */
    @Override
    void clearKB() {
        this.KB = new ArrayList<>();
    }

    /**
     * Sweeps through board using solves using agent specific strategy.
     */
    @Override
    public void sweep(boolean verbose) {
        boolean SpsResult = sps(verbose);
        if (SpsResult) {
            System.out.println("Final map\n");
            printAgentBoard();
            System.out.println("\nResult: Agent alive: all solved\n");
        } else {
            generateCellMap();
            for (int i = 0; i < getInitialCoveredCount(); i++) {
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

    /**
     * Get the number of initial covered cells.
     * @return number of covered cells.
     */
    private int getInitialCoveredCount() {
        int coverCount = 0;
        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                if (agentBoard[i][j] == '?') {
                    coverCount++;
                }
            }
        }
        return coverCount;
    }

    /**
     * One iteration of solving the game.
     * @param verbose prints world with each iteration.
     */
    @Override
    public void solve(boolean verbose) {
        generateKB();
        SATSweep(verbose);
        sps(verbose);
        generateKB();
    }

    /**
     * For a covered cell, generate logic.
     * @param currentCell covered cell to generate logic based on.
     */
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

    /**
     * Given a cell, create all the logic options for danger in the surrounding cells.
     * @param dangerSubset the number of options containing cells to be marked as dangerous.
     * @param neighbour numeric cell at centre of danger subsets.
     * @return logic sentence that's to be added to the KB.
     */
    public void generateSentence(List<Set<Integer>> dangerSubset, Cell neighbour, boolean danger) {
        ArrayList<Cell> adjacentCovered = super.getApplicableNeighbours(neighbour, '?');

        for (Set subset: dangerSubset) {
            int[] option = generateOption(subset, adjacentCovered, danger);
            if (option.length > 0) {
                KB.add(option);
            }
        }
    }

    /**
     * Generates the logic based on the provided Cell combinations.
     * @param subset Cell combinations.
     * @param adjacentCells Cells around numeric cell.
     * @return String of logic in DNF.
     */
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

    /**
     * Loop through each position in the world and attempt to SAT solve it.
     * @param verbose prints world with each iteration.
     */
    @Override
    void SATSweep(boolean verbose) {
        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                Cell currentCell = new Cell(i, j);

                if (agentBoard[i][j] == '?') {
                    if (verbose) {
                        printAgentBoard();
                    }
                    try {
                        entailmentChecks(currentCell, verbose);
                        sps(verbose);
                        generateKB();
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    /**
     * Checks for danger and non-danger entailment.
     * @param currentCell Cell to be checked.
     * @throws TimeoutException in event of parsing failure of SAT input.
     */
    private void entailmentChecks(Cell currentCell, boolean verbose) throws TimeoutException {
        if (!entails(currentCell, true) && !entails(currentCell, false)) {
            return;
        } else if (!entails(currentCell, true)) {
            flag(currentCell.getRow(), currentCell.getCol());
            sps(verbose);
            generateKB();
        } else if (!entails(currentCell, false)) {
            probe(currentCell.getRow(), currentCell.getCol());
            sps(verbose);
            generateKB();
        }
    }

    /**
     * Run entailment checks for a given covered cell.
     * @param coveredCell cell to be checked.
     * @param danger toggle for danger/non-danger.
     * @return true if satisfiable.
     * @throws TimeoutException in event of parsing failure of SAT input.
     */
    private boolean entails(Cell coveredCell, boolean danger) throws TimeoutException {
        int cellNum = cellNumberMap.get(coveredCell);
        if (danger) {
            cellNum = -cellNum;
        }
        int[] singleArray = new int[]{cellNum};
        ArrayList<int[]> satInput = new ArrayList<>(KB);
        satInput.add(singleArray);

        return sat4jSolver(satInput, cellNumberMap);
    }

    /**
     * Provided with a central Cell, takes its value and number of adjacent covered Cells.
     * Then calculated all the different non-danger subsets.
     * @param neighbour numeric neighbour of a covered Cell.
     * @return List of all possible danger combinations.
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
        } else if (numAdjacentCovered < numAdjacentMines) {
            Set<Integer> set = new HashSet<>();
            set.add(0);
            List<Set<Integer>> defaultList = new ArrayList<>();
            defaultList.add(set);
            return defaultList;
        }

        return getKCombinations(initialSet, numNonDangers);
    }

    /**
     * Provided with a central Cell, takes its value and number of adjacent covered Cells.
     * Then calculated all the different danger subsets.
     * @param neighbour numeric neighbour of a covered Cell.
     * @return List of all possible danger combinations.
     */
    @Override
    public List<Set<Integer>> getDangerSubsets(Cell neighbour) {

        char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];
        int numAdjacentMines = Character.getNumericValue(cellValue);
        int numAdjacentCovered = getNumApplicableNeighbours(neighbour, '?');
        int numAdjacentFlagged = getNumApplicableNeighbours(neighbour, '*');

        List<Integer> initialSet = getIntegerList(numAdjacentCovered);

        if (numAdjacentMines == 1) {
            return getKCombinations(initialSet, initialSet.size());
        }

        return getKCombinations(initialSet, numAdjacentMines - numAdjacentFlagged);
    }

    /**
     * Sat solver method.
     * @param clauses knowledge base and additional clause.
     * @param mapping used to determine number of clauses.
     * @return true if satisfiable.
     * @throws TimeoutException in event of parsing failure of SAT input.
     */
    private boolean sat4jSolver(ArrayList<int[]> clauses, HashMap<Cell, Integer> mapping) throws TimeoutException {

        int numVariables = mapping.size();
        int numClauses = clauses.size();
        ISolver solver = SolverFactory.newDefault();

        solver.newVar(numVariables);
        solver.setExpectedNumberOfClauses(numClauses);

        for (int[] clause : clauses) {
            try {
                solver.addClause(new VecInt(clause));
            } catch (ContradictionException con) {
                return false;
            }
        }

        IProblem problem = solver;
        if (problem.isSatisfiable()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Debugging method for Sat input.
     * @param clauses KB and additional clause.
     */
    private void printSatInput(ArrayList<int[]> clauses) {
        for (int[] arr : clauses) {
            System.out.print(Arrays.toString(arr));
        }
        System.out.println();
    }
}
