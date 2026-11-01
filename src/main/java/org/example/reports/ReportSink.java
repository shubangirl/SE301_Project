package org.example.reports;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/** Where finished report lines go. */
public interface ReportSink {
    void write(Path reportFile, List<String> reportLines) throws IOException;
}
