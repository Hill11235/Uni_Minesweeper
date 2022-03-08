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

    @Override
    public void solve(boolean verbose) {
        generateKB();
        SATSweep(verbose);
        sps(verbose);
        generateKB();
    }

    @Override
    public void createSentence(Cell currentCell) {
        ArrayList<Cell> adjacentCells = currentCell.getAdjacentCells(agentBoard.length);
        //System.out.println("? Cell: " + currentCell.toString());
        for (Cell neighbour : adjacentCells) {
            char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];
            //System.out.println("Neighbour: " + neighbour.toString());

            if (cellValue != 'b' && cellValue != '?' && cellValue != '*') {
                //System.out.println("Neighbour past if: " + neighbour.toString());
                List<Set<Integer>> dangerSubsets = getDangerSubsets(neighbour);
                //System.out.println("Danger subsets size: " + dangerSubsets.size());
                generateSentence(dangerSubsets, neighbour, true);
                List<Set<Integer>> nonDangerSubsets = getNonDangerSubsets(neighbour);
                //System.out.println("Non-danger subsets size: " + nonDangerSubsets.size());
                generateSentence(nonDangerSubsets, neighbour, false);
            }
        }
    }

    public void generateSentence(List<Set<Integer>> dangerSubset, Cell neighbour, boolean danger) {
        ArrayList<Cell> adjacentCovered = super.getApplicableNeighbours(neighbour, '?');

        for (Set subset: dangerSubset) {
            int[] option = generateOption(subset, adjacentCovered, danger);
            if (option.length > 0) {
                KB.add(option);
            }
        }
    }

    private int[] generateOption(Set<Integer> subset, ArrayList<Cell> adjacentCells, boolean danger) {
        ArrayList<Integer> arrayBuilder = new ArrayList<>();

        for (Integer index : subset) {
            Cell currentCell = adjacentCells.get(index);
            //System.out.println("Cell used to generate option: " + currentCell.toString());
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
                        entailmentChecks(currentCell, verbose);
                        sps(verbose);
                        generateKB();
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

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

    private boolean entails(Cell coveredCell, boolean danger) throws TimeoutException {
        //System.out.println("Cell check: " + coveredCell.toString());
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
        } else if (numAdjacentCovered < numAdjacentMines) {
            Set<Integer> set = new HashSet<>();
            set.add(0);
            List<Set<Integer>> defaultList = new ArrayList<>();
            defaultList.add(set);
            return defaultList;
        }

        return getKCombinations(initialSet, numNonDangers);
    }

    @Override
    public List<Set<Integer>> getDangerSubsets(Cell neighbour) {

        char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];
        int numAdjacentMines = Character.getNumericValue(cellValue);
        int numAdjacentCovered = getNumApplicableNeighbours(neighbour, '?');
        int numAdjacentFlagged = getNumApplicableNeighbours(neighbour, '*');
        //System.out.println("Number adjacent covered cell: " + numAdjacentCovered);

        List<Integer> initialSet = getIntegerList(numAdjacentCovered);

        if (numAdjacentMines == 1) {
            return getKCombinations(initialSet, initialSet.size());
        }

        return getKCombinations(initialSet, numAdjacentMines - numAdjacentFlagged);
    }

    private boolean sat4jSolver(ArrayList<int[]> clauses, HashMap<Cell, Integer> mapping) throws TimeoutException {

        int numVariables = mapping.size();
        int numClauses = clauses.size();
        ISolver solver = SolverFactory.newDefault();

        solver.newVar(numVariables);
        solver.setExpectedNumberOfClauses(numClauses);
        //System.out.println("Number of variables: " + numVariables);
        //System.out.println("Number of clauses: " + numClauses);
        //printSatInput(clauses);

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

    private void printSatInput(ArrayList<int[]> clauses) {
        for (int[] arr : clauses) {
            System.out.println(Arrays.toString(arr));
        }

    }
}
