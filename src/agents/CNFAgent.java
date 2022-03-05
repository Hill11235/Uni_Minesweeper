package agents;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
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
    //  X convert entailment KB to DIMACS
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
        SATSweep(verbose);
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
                    try {
                        entailmentChecks(currentCell);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void entailmentChecks(Cell currentCell) throws ContradictionException, TimeoutException {
        if (!entails(currentCell, false)) {
            probe(currentCell.getRow(), currentCell.getCol());
        } else if (!entails(currentCell, true)) {
            flag(currentCell.getRow(), currentCell.getCol());
        }
    }

    private boolean entails(Cell coveredCell, boolean danger) throws ContradictionException, TimeoutException {
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

    private boolean sat4jSolver(ArrayList<int[]> clauses, HashMap<Cell, Integer> mapping) throws ContradictionException, TimeoutException {

        int numVariables = mapping.size();
        int numClauses = clauses.size();
        ISolver solver = SolverFactory.newDefault();

        solver.newVar(numVariables);
        solver.setExpectedNumberOfClauses(numClauses);

        for (int[] clause : clauses) {
            solver.addClause(new VecInt(clause));
        }

        IProblem problem = solver;
        if (problem.isSatisfiable()) {
            return true;
        } else {
            return false;
        }
    }

}
