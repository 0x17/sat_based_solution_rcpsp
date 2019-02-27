package scheduler.mapping;


import scheduler.data.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneralParser implements IProjectParser {

    private final Mapper mapper;
    private final IProjectParser ksdParser;
    private final IProjectParser pattersonParser;

    public GeneralParser(Mapper mapper) {
        this.mapper = mapper;
        this.ksdParser = new PSPLIBParser();
        this.pattersonParser = new PattersonParser();
    }

    private Stream<String> nonEmptyParts(String line) {
        return Arrays.stream(line.split(" ")).filter(part -> !part.isEmpty());
    }

    private int numNonEmptyParts(String line) {
        return (int)nonEmptyParts(line).count();
    }

    private boolean isPatterson(List<String> lines) {
        List<String> actualLines = lines.stream().filter(line -> line.trim().length() > 0).collect(Collectors.toList());
        if(numNonEmptyParts(actualLines.get(0)) != 2) return false;
        int nres = Integer.valueOf(nonEmptyParts(actualLines.get(0)).collect(Collectors.toList()).get(1));
        return numNonEmptyParts(actualLines.get(1)) == nres;
    }

    private boolean isKSD(List<String> lines) {
        if(!lines.get(0).contains("*")) return false;
        if(!lines.get(1).contains("file with basedata")) return false;
        return lines.get(3).contains("*");
    }

    private boolean isAmmann(List<String> lines) {
        return lines.stream().allMatch(line -> line.isEmpty() || line.split(";").length == 4);
    }

    public Project parse(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        if(isPatterson(lines)) {
            return pattersonParser.parse(path);
        } else if(isKSD(lines)) {
            return ksdParser.parse(path);
        } else if(mapper != null && isAmmann(lines)) {
            return mapper.readProject(path);
        } else {
            throw new IOException("Unknown project file format, please use Patterson, KSD or Ammann format.;");
        }
    }

}
