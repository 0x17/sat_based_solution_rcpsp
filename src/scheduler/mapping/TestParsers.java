package scheduler.mapping;

import scheduler.algorithm.Algorithm;
import scheduler.data.Project;
import scheduler.log.Log;

import java.io.IOException;

public class TestParsers {

    public static void main(String[] args) throws IOException {
        Project project;
        Algorithm rcpsp;
        Algorithm fwd;

        boolean bccMode = true;
        String projectPath = "RG300_1.rcp";
        Log.setLogPath("mylog.txt");
        Log.i(projectPath);

        Mapper mapper = Mapper.getMapper();
        GeneralParser gp = new GeneralParser(mapper);

        Project p = gp.parse(projectPath);

        System.out.println("Test");

        /*project = mapper.readProject(projectPath);
        fwd = new FowardAlgorithm(project);
        rcpsp = new RCPSPAlgorithm(project, bccMode);
        fwd.calculate();
        rcpsp.calculate();*/
    }

}
