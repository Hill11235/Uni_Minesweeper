package agents;

import support.GameState;

/**
 * P3 class for using a SAT solver to solve problem using KB in DNF.
 */
public class DNFAgent extends BeginnerAgent{

    public DNFAgent(GameState game) {
        super(game);
    }

    //KB in ‘DNF’ form
    //check entailment with a SAT solver
    //parse and solve using LogicNG

    //run SPS from BeginnerAgent, if true then done
    //if false, then run SAT solver on resulting board

    //TODO consider ultimate scenarios with SAT solver. Can this fail?
    @Override
    public void sweep(boolean verbose) {
        boolean SpsResult = super.sweepLoop(verbose);
        if (SpsResult) {
            System.out.println("Final map\n");
            printAgentBoard();
            System.out.println("\nResult: Agent alive: all solved\n");
        } else {
            boolean DnfResult = sweepLoop(verbose);
            if (DnfResult) {
                System.out.println("Final map\n");
                printAgentBoard();
                System.out.println("\nResult: Agent alive: all solved\n");
            } else {
                //TODO complete this scenario
            }
        }
    }

    @Override
    boolean sweepLoop(boolean verbose) {

        return false;
    }

    /**
     * Want to check KB entails ~D in cell, check SAT of KB & D, if false, probe.
     * @return boolean indicating satisifiability.
     */
    private boolean entailsNoDanger() {
        return false;
    }

    /**
     * Want to check KB entails D in cell, check SAT of KB & ~D, if false, flag.
     * @return boolean indicating satisifiability.
     */
    private boolean entailsDanger() {
        return false;
    }
}
