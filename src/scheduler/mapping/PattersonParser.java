package scheduler.mapping;

import scheduler.data.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static scheduler.utils.Utils.intPartsArr;
import static scheduler.utils.Utils.integerToIntArray;

public class PattersonParser  extends AbstractParser implements IProjectParser {

    @Override
    public Project parse(String path) throws IOException {
        boolean cardinalitiesKnown = false, continueSuccs = false;
        int succRemaining = 0, j = 0;

        reset();

        List<String> lines = Files.lines(Paths.get(path)).collect(Collectors.toList());

        for(String line : lines) {
            final Integer[] values = intPartsArr(line);

            if(succRemaining > 0) {
                int k;
                for(k=0; k<succRemaining && k<values.length; k++) {
                    adjMx[j][values[k]-1] = true;
                }
                succRemaining -= k;
                if(succRemaining <= 0) {
                    j++;
                }
            }
            // #jobs #res
            else if(values.length == 2) {
                numJobs = values[0];
                numRes = values[1];
                adjMx = new boolean[numJobs][numJobs];
                durations = new int[numJobs];
                demands = new int[numJobs][numRes];
                capacities = new int[numRes];
                cardinalitiesKnown = true;

            } else if(cardinalitiesKnown) {
                // K1 K2 ...
                if(values.length == numRes) {
                    capacities = integerToIntArray(values);
                    // dj kj1 kj2 .. #succs succ1 succ2 ...
                } else if(values.length >= 1 + numRes + 1) {
                    durations[j] = values[0];
                    for(int r=0; r<numRes; r++)
                        demands[j][r] = values[1+r];
                    int succCount = values[1+numRes];
                    int k;
                    for(k=0; k<succCount && k+2+numRes<values.length; k++) {
                        int vix = 2+numRes+k;
                        adjMx[j][values[vix]-1] = true;
                    }
                    if(k+2+numRes >= values.length && k < succCount) {
                        succRemaining = succCount - k;
                    } else {
                        j++;
                    }
                }
            }
        }

        return objFromPrimitiveTypes(Paths.get(path).getFileName().toString());
    }


}
