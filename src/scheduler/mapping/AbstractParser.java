package scheduler.mapping;

import scheduler.data.*;

abstract class AbstractParser {

    int numJobs = 0, numPeriods = 0, numRes = 0;
    int[] durations = null, capacities = null;
    int[][] demands = null;
    boolean[][] adjMx = null;

    Project objFromPrimitiveTypes(String name) {
        Project p = new Project(name, 0, Integer.MAX_VALUE);

        for(int r=0; r<numRes; r++) {
            p.addResource(new Resource(r, "Resource " + r, capacities[r]));
        }

        for(int i=0; i<numJobs; i++) {
            Activity a = new Activity(i, "Task "+ i, durations[i]);
            for(int r=0; r<numRes; r++) {
                a.addConsumption(p.getResourceById(r), demands[i][r]);
            }
            p.addActivity(a);
        }

        for(int i=0; i<numJobs; i++) {
            for(int k=0; k<numJobs; k++) {
                if(adjMx[i][k]) {
                    Activity a = p.getActivityById(i);
                    Activity b = p.getActivityById(k);
                    p.addRelation(new Relation(a, b, RelationType.FS));
                }
            }
        }

        return p;
    }

    void reset() {
        adjMx = null;
        numJobs = 0;
        numRes = 0;
        this.durations = null;
        this.capacities = null;
        this.demands = null;
    }

}
