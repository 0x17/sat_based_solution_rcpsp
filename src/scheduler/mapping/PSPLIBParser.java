package scheduler.mapping;

import scheduler.data.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static scheduler.utils.Utils.extractInt;
import static scheduler.utils.Utils.intParts;

public class PSPLIBParser extends AbstractParser implements IProjectParser {
    @Override
    public Project parse(String path) throws IOException {
        reset();
        parseProGen(path);
        return objFromPrimitiveTypes(Paths.get(path).getFileName().toString());
    }

    private void parseProGen(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));

        int ctr = 0;

        for(String line : lines) {
            if(line.startsWith("jobs (incl. supersource/sink )")) {
                numJobs = extractInt(line);
                adjMx = new boolean[numJobs][numJobs];
                durations = new int[numJobs];
            }
            else if(line.startsWith("horizon")) {
                numPeriods = extractInt(line);
            }
            else if(line.trim().startsWith("- renewable")) {
                numRes = extractInt(line.replace("R", " "));
                capacities = new int[numRes];
                demands = new int[numJobs][numRes];
            }
            else if(line.startsWith("PRECEDENCE RELATIONS:")) parsePrecedenceRelation(lines, ctr);
            else if(line.startsWith("REQUESTS/DURATIONS:")) parseDemandsAndDurations(lines, ctr);
            else if(line.startsWith("RESOURCEAVAILABILITIES:")) parseCapacities(lines, ctr);

            ctr++;
        }
    }

    private void parseCapacities(List<String> lines, int ctr) {
        ArrayList<Integer> caps = intParts(lines.get(ctr + 2));
        for(int i=0; i<caps.size(); i++)
            capacities[i] = caps.get(i);
    }

    private void parseDemandsAndDurations(List<String> lines, int ctr) {
        for(int i=0; i<numJobs; i++) {
            int ix = ctr + 3 + i;
            ArrayList<Integer> parts = intParts(lines.get(ix));
            durations[parts.get(0)-1] = parts.get(2);
            for(int j=3; j<parts.size(); j++) {
                demands[parts.get(0)-1][j-3] = parts.get(j);
            }
        }
    }

    private void parsePrecedenceRelation(List<String> lines, int ctr) {
        for(int i=0; i<numJobs; i++) {
            int ix = ctr + 2 + i;
            ArrayList<Integer> parts = intParts(lines.get(ix));
            for(int j=3; j<parts.size(); j++) {
                adjMx[parts.get(0)-1][parts.get(j)-1] = true;
            }
        }
    }
}
