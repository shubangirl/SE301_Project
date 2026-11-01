package org.example.reports;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ReportWriter {
    public void write(Path reportFile, List<String> reportLines) throws IOException {
        Files.createDirectories(reportFile.getParent());
        Files.write(
                reportFile,
                reportLines,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
    }
}
