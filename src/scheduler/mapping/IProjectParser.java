package scheduler.mapping;

import scheduler.data.Project;

import java.io.IOException;

public interface IProjectParser {
    public Project parse(String path) throws IOException;
}
