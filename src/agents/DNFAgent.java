package agents;

import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;
import support.Cell;
import support.GameState;

import java.util.*;

/**
 * P3 class for using a SAT solver to solve problem using KB in DNF.
 */
public class DNFAgent extends BeginnerAgent{

    final ArrayList<Cell> addedToKB = new ArrayList<>();
    final HashMap<Cell, Character> cellLetterMap = new HashMap<>();
    int charIncrementer = 65;
    String KB = "";

    public DNFAgent(GameState game) {
        super(game);
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

    /**
     * Runs the SAT solver across all cells.
     * @param verbose prints world with each iteration.
     */
    void solve(boolean verbose) {
        //generateKB();
        sps(verbose);
        generateKB();
        SATSweep(verbose);
    }

    /**
     * Maps each covered cell in the board to a char that can be used for SAT solver.
     */
    //TODO check this approach doesn't cause issues on the larger configs where there might be more covered cells than letters.
    void generateCellMap() {
        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                Cell currentCell = new Cell(i, j);

                if (agentBoard[i][j] == '?') {
                    cellLetterMap.put(currentCell, (char) charIncrementer);
                    charIncrementer++;
                }
            }
        }
    }

    /**
     * Generate the logic from each covered cell for the knowledge base.
     */
    void generateKB() {
        clearKB();
        addedToKB.clear();

        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                Cell currentCell = new Cell(i, j);

                if (agentBoard[i][j] == '?') {
                    createSentence(currentCell);
                }
            }
        }
    }

    /**
     * Resets the knowledge base.
     */
    void clearKB() {
        this.KB = "";
    }


    /**
     * Loop through each position in the world and attempt to SAT solve it.
     * @param verbose prints world with each iteration.
     */
    void SATSweep(boolean verbose) {
        for (int i = 0; i < agentBoard.length; i++) {
            for (int j = 0; j < agentBoard.length; j++) {
                Cell currentCell = new Cell(i, j);

                if (agentBoard[i][j] == '?') {
                    if (verbose) {
                        printAgentBoard();
                    }
                    try {
                        //System.out.println("Cell in sweep: " + currentCell.toString());
                        entailmentChecks(currentCell);
                        sps(verbose);
                        generateKB();
                    } catch (ParserException ignored) {
                    }
                }
            }
        }
    }

    /**
     * Checks for danger and non-danger entailment.
     * @param currentCell Cell to be checked.
     * @throws ParserException in event of parsing failure of SAT input.
     */
    private void entailmentChecks(Cell currentCell) throws ParserException {
        generateKB();
        if (!entailsNoDanger(currentCell) && !entailsDanger(currentCell)) {
            return;
        } else if (!entailsNoDanger(currentCell)) {
            //System.out.println("probing");
            probe(currentCell.getRow(), currentCell.getCol());
            generateKB();
        } else if (!entailsDanger(currentCell)) {
            //System.out.println("flagging");
            flag(currentCell.getRow(), currentCell.getCol());
            generateKB();
        }
    }

    /**
     * Want to check KB entails ~D in cell, check SAT of KB & D, if false, probe.
     * @return boolean indicating satisifiability.
     */
    private boolean entailsNoDanger(Cell coveredCell) throws ParserException {
        //System.out.println("Entails no danger check");
        FormulaFactory f = new FormulaFactory();
        PropositionalParser p = new PropositionalParser(f);
        String satInput = "(" + KB + ") & " + cellLetterMap.get(coveredCell).toString();
        //System.out.println("SatInput no danger: " + satInput);
        Formula formula = p.parse(satInput);
        //System.out.println("entailsNoDangerFormula: " + formula.toString());

        SATSolver miniSat = MiniSat.miniSat(f);
        miniSat.add(formula);
        Tristate result = miniSat.sat();
        //System.out.println("Entails no danger result: " + result.toString());
        //System.out.println("Converts to: " + Objects.equals(result.toString(), "TRUE"));
        return Objects.equals(result.toString(), "TRUE");
    }

    /**
     * Want to check KB entails D in cell, check SAT of KB & ~D, if false, flag.
     * @return boolean indicating satisifiability.
     */
    private boolean entailsDanger(Cell coveredCell) throws ParserException {
        //System.out.println("Entails danger check");
        FormulaFactory f = new FormulaFactory();
        PropositionalParser p = new PropositionalParser(f);
        String satInput = "(" + KB + ") & ~" + cellLetterMap.get(coveredCell).toString();
        //System.out.println("SatInput danger: " + satInput);
        Formula formula = p.parse(satInput);
        //System.out.println("entailsDangerFormula: " + formula.toString());

        SATSolver miniSat = MiniSat.miniSat(f);
        miniSat.add(formula);
        Tristate result = miniSat.sat();
        //System.out.println("Entails danger result: " + result.toString());
        //System.out.println("Converts to: " + Objects.equals(result.toString(), "TRUE"));
        return Objects.equals(result.toString(), "TRUE");
    }

    /**
     * Add a generated sentence to the KB.
     */
    void addToKB(String sentence) {
        if (this.KB.equals("")) {
            this.KB = sentence;
        } else {
            this.KB += " & " + sentence;
        }
    }

    /**
     * For a given covered cell, generate logic sentences and add to the KB.
     * @param currentCell covered cell to generate logic based on.
     */
    public void createSentence(Cell currentCell) {
        ArrayList<Cell> adjacentCells = currentCell.getAdjacentCells(agentBoard.length);

        for (Cell neighbour : adjacentCells) {
            char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];

            if (cellValue != 'b' && cellValue != '?' && cellValue != '*' && !addedToKB.contains(neighbour)) {
                //System.out.println("Cell used to generate sentence: " + neighbour.toString());
                List<Set<Integer>> dangerSubsets = getDangerSubsets(neighbour);
                String sentence = generateSentence(dangerSubsets, neighbour);
                //System.out.println("Sentence generated: " + sentence);
                addToKB(sentence);
                addedToKB.add(neighbour);
            }
        }
    }

    /**
     * Given a cell, create all the logic options for danger in the surrounding cells.
     * @param dangerSubsets the number of options containing cells to be marked as dangerous.
     * @param neighbour numeric cell at centre of danger subsets.
     * @return logic sentence that's to be added to the KB.
     */
    private String generateSentence(List<Set<Integer>> dangerSubsets, Cell neighbour) {
        StringBuilder sentence = new StringBuilder("");
        ArrayList<Cell> adjacentCovered = super.getApplicableNeighbours(neighbour, '?');

        for (Set subset: dangerSubsets) {
            String option = generateOption(subset, adjacentCovered);
            if (sentence.length() > 0) {
                sentence.append(" | ");
            }
            sentence.append(option);
        }
        if (sentence.length() != 0) {
            sentence.append(")");
            sentence.insert(0, "(");
        }

        return sentence.toString();
    }

    /**
     * Generates the logic based on the provided Cell combinations.
     * @param subset Cell combinations.
     * @param adjacentCells Cells around numeric cell.
     * @return String of logic in DNF.
     */
    private String generateOption(Set<Integer> subset, ArrayList<Cell> adjacentCells) {
        StringBuilder option = new StringBuilder("");
        int numAdjacentCovered = adjacentCells.size();

        for (int i = 0; i < numAdjacentCovered; i++) {

            if (i > 0) {
                option.append(" & ");
            }
            Cell currentCell = adjacentCells.get(i);
            String clause = cellLetterMap.get(currentCell).toString();
            if (!subset.contains(i)) {
                clause = "~" + clause;
            }
            option.append(clause);
        }
        if (option.length() != 0) {
            option.append(")");
            option.insert(0, "(");
        }

        return option.toString();
    }

    /**
     * Provided with a central Cell, takes its value and number of adjacent covered Cells.
     * Then calculated all the different danger subsets.
     * @param neighbour numeric neighbour of a covered Cell.
     * @return List of all possible danger combinations.
     */
    public List<Set<Integer>> getDangerSubsets(Cell neighbour) {

        char cellValue = agentBoard[neighbour.getRow()][neighbour.getCol()];
        int numAdjacentMines = Character.getNumericValue(cellValue);
        int numAdjacentCovered = getNumApplicableNeighbours(neighbour, '?');
        int numAdjacentFlagged = getNumApplicableNeighbours(neighbour, '*');

        //System.out.println("Number adjacent covered cell: " + numAdjacentCovered);
        //System.out.println("Number adjacent mines: " + numAdjacentMines);

        List<Integer> initialSet = getIntegerList(numAdjacentCovered);

        return getKCombinations(initialSet, numAdjacentMines - numAdjacentFlagged);
    }

    /**
     * Creates list populated with numbers 0 up to numAdjacentCovered - 1.
     * @param numAdjacentCovered size of list to be generated.
     * @return list of integers.
     */
    List<Integer> getIntegerList(int numAdjacentCovered) {
        List<Integer> initialSet = new ArrayList<>();

        for (int i = 0; i < numAdjacentCovered; i++) {
            initialSet.add(i);
        }
        return initialSet;
    }

    /**
     * Overloaded method which initiates the recursive method to get all the subsets of size k (subsetSize).
     * @param initialSet set from which subsets are to be chosen.
     * @param subsetSize size of the subsets to be created.
     * @return a list of all subsets of requested size.
     */
    public List<Set<Integer>> getKCombinations(List<Integer> initialSet, int subsetSize) {
        List<Set<Integer>> combinations = new ArrayList<>();
        getKCombinations(initialSet, subsetSize, 0, new HashSet<Integer>(), combinations);
        return combinations;
    }

    /**
     * Find all subsets of size k in a set of size n. Will create n choose k subsets.
     * @param initialSet set from which subsets are to be chosen.
     * @param subsetSize size of the subsets to be created.
     * @param index index of element from initialSet to be added.
     * @param currentSet set being built throughout the recursion.
     * @param solution the list of subsets that is being added to throughout.
     */
    private void getKCombinations(List<Integer> initialSet, int subsetSize, int index, Set<Integer> currentSet, List<Set<Integer>> solution) {

        if (currentSet.size() == subsetSize) {
            solution.add(new HashSet<>(currentSet));
            return;
        }

        if (index == initialSet.size()) {
            return;
        }

        Integer randomIndex = initialSet.get(index);
        currentSet.add(randomIndex);

        //recursion with randomIndex included in the subset
        getKCombinations(initialSet, subsetSize, index + 1, currentSet, solution);

        //recursion with randomIndex not included in the subset
        currentSet.remove(randomIndex);
        getKCombinations(initialSet, subsetSize, index + 1, currentSet, solution);
    }
}
