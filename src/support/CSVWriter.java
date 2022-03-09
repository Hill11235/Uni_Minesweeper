package support;

import agents.BasicAgent;
import agents.BeginnerAgent;
import agents.CNFAgent;
import agents.DNFAgent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class for writing results of algorithms to a CSV.
 */
public class CSVWriter {

    /**
     * Main method for writing the file.
     * @param args generic argument.
     */
    public static void main(String[] args) {
        CSVWriter cw = new CSVWriter();
        try (PrintWriter pw = new PrintWriter("SweeperResults.csv")) {
            cw.singleWriter(pw);
        } catch (FileNotFoundException fnf) {
            System.out.println(fnf.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes results of sweepers.
     * @param pw PrintWriter to write to.
     * @throws IOException thrown if any issues encountered when writing.
     */
    public void singleWriter(PrintWriter pw) throws IOException {
        StringBuilder sb = new StringBuilder();
        titleRow(sb);

        for (World world : World.values()) {
            BasicAgent[] agents = createAgents(world);
            int i = 1;
            for (BasicAgent agent : agents) {
                sb.append(world.name());
                sb.append(",");
                sb.append("P" + i);
                sb.append(",");
                agent.sweep(false);
                sb.append(agent.getGame().isWon());
                sb.append(",");
                sb.append(agent.getGame().isLost());
                sb.append(",");
                sb.append(world.map[0].length * world.map[0].length);
                sb.append(",");
                sb.append(agent.getNumCoveredCells());
                sb.append("\n");

                i++;
            }
        }

        pw.write(sb.toString());
    }

    /**
     * Create array with each agent.
     * @param world game to be played.
     * @return array with each agent.
     */
    public BasicAgent[] createAgents(World world) {
        BasicAgent[] arr = new BasicAgent[4];
        arr[0] = new BasicAgent(new GameState(world));
        arr[1] = new BeginnerAgent(new GameState(world));
        arr[2] = new DNFAgent(new GameState(world));
        arr[3] = new CNFAgent(new GameState(world));

        return arr;
    }

    /**
     * Creates title row for csv.
     * @param sb StringBuilder used to construct csv.
     */
    public void titleRow(StringBuilder sb) {
        sb.append("World");
        sb.append(",");
        sb.append("Agent");
        sb.append(",");
        sb.append("Won");
        sb.append(",");
        sb.append("Lost");
        sb.append(",");
        sb.append("Total squares in game");
        sb.append(",");
        sb.append("Covered squares left");
        sb.append(",");
        sb.append("\n");
    }
}
