package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.example.alerts.AlertRepository;
import org.example.alerts.AlertService;
import org.example.chamber.Chamber;
import org.example.crop.CropCatalog;
import org.example.crop.CropRegistry;
import org.example.devices.ControlCenter;
import org.example.monitoring.ChamberMonitor;
import org.example.monitoring.ResourceUsage;
import org.example.reports.ReportService;
import org.example.reports.ReportSink;
import org.example.reports.ReportWriter;

/**
 * Composition root: chooses the implementations and wires them together.
 * Contains no business logic.
 */
public class SVFRMS {
    private static final Path REPORT_FILE = Paths.get("archive").resolve("daily_report.txt");
    private static final Duration MONITORING_TIMEOUT = Duration.ofMinutes(5);

    /** Which crop each chamber grows. Add a line here to add a chamber. */
    private static final Map<String, String> CHAMBER_CROPS = new LinkedHashMap<>();
    static {
        CHAMBER_CROPS.put("CH1", "Tomato");
        CHAMBER_CROPS.put("CH2", "Strawberry");
        CHAMBER_CROPS.put("CH3", "Lettuce");
        CHAMBER_CROPS.put("CH4", "Chili");
        CHAMBER_CROPS.put("CH5", "Bell Pepper");
        // CHAMBER_CROPS.put("CH6", "Kale"); // Kale demo: uncomment
    }

    public static void main(String[] args) throws Exception {
        CropCatalog crops = CropRegistry.withDefaults();
        List<Chamber> chambers = createChambers(crops);

        AlertService alerts = new AlertService(new AlertRepository());
        ResourceUsage usage = new ResourceUsage();
        ControlCenter controls = new ControlCenter();

        List<ChamberMonitor> monitors = chambers.stream()
                .map(chamber -> new ChamberMonitor(chamber, alerts, usage, controls))
                .toList();
        new SimulationRunner(controls, MONITORING_TIMEOUT).run(monitors);

        // Archive first, then report: deterministic order removes the old
        // archive/report race.
        int archivedThisRun = alerts.archiveAlerts().size();
        List<String> report = new ReportAssembler(new ReportService(), alerts, usage)
                .dailyReport(chambers, archivedThisRun);

        report.forEach(System.out::println);
        System.out.println("System finished sensor processing.");

        ReportSink sink = new ReportWriter();
        sink.write(REPORT_FILE, report);
        System.out.println("Archive report saved to: " + REPORT_FILE.toAbsolutePath());
    }

    private static List<Chamber> createChambers(CropCatalog crops) {
        return CHAMBER_CROPS.entrySet().stream()
                .map(entry -> new Chamber(entry.getKey(), crops.get(entry.getValue())))
                .toList();
    }
}
