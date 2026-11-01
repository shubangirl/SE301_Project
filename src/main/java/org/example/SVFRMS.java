package org.example;

import org.example.alerts.AlertRepository;
import org.example.alerts.AlertService;
import org.example.chamber.Chamber;
import org.example.crop.CropRegistry;
import org.example.devices.ControlCenter;
import org.example.monitoring.ChamberMonitor;
import org.example.monitoring.ResourceUsage;
import org.example.reports.ReportService;
import org.example.reports.ReportWriter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Composition root: creates the components and wires them together. Holds no mutable static state. */
public class SVFRMS {
    private static final Path REPORT_FILE = Paths.get("archive").resolve("daily_report.txt");

    public static void main(String[] args) throws Exception {
        CropRegistry crops = CropRegistry.withDefaults();
        List<Chamber> chambers = List.of(
                new Chamber("CH1", crops.get("Tomato")),
                new Chamber("CH2", crops.get("Strawberry")),
                new Chamber("CH3", crops.get("Lettuce")),
                new Chamber("CH4", crops.get("Chili")),
                new Chamber("CH5", crops.get("Bell Pepper")));

        AlertService alerts = new AlertService(new AlertRepository());
        ResourceUsage usage = new ResourceUsage();
        ControlCenter controls = new ControlCenter();

        Thread controlThread = new Thread(controls, "control-center");
        controlThread.start();

        ExecutorService pool = Executors.newFixedThreadPool(chambers.size());
        for (Chamber chamber : chambers) {
            pool.submit(new ChamberMonitor(chamber, alerts, usage, controls));
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.MINUTES);

        controls.shutdown();
        controlThread.join();

        // Archive first, then report: deterministic order removes the old archive/report race.
        int moved = alerts.archiveAlerts().size();
        String note = moved > 0 ? "Archived " + moved + " alerts" : "No alerts to archive";

        List<String> chamberLines = chambers.stream()
                .sorted(Comparator.comparing(Chamber::id))
                .map(c -> c.id() + " -> " + c.summary().describe())
                .toList();

        ReportService reportService = new ReportService();
        List<String> additionalReports = new java.util.ArrayList<>();
        additionalReports.addAll(reportService.createWeeklyCropReport(chamberLines));
        additionalReports.addAll(reportService.createWaterUsageReport(usage.water()));
        additionalReports.addAll(reportService.createEnergyConsumptionReport(usage.energy()));
        additionalReports.addAll(reportService.createNutrientConsumptionReport(usage.nutrient()));

        List<String> report = reportService.createDailyReport(
                alerts.getTotalAlertsGenerated(),
                alerts.getActiveAlertCount(),
                alerts.getArchivedAlerts().size(),
                usage.water(),
                usage.energy(),
                combineEntries(note, additionalReports),
                chamberLines,
                alerts.getArchivedAlerts());

        report.forEach(System.out::println);
        System.out.println("System finished sensor processing.");
        new ReportWriter().write(REPORT_FILE, report);
        System.out.println("Archive report saved to: " + REPORT_FILE.toAbsolutePath());
    }

    private static List<String> combineEntries(String note, List<String> additionalReports) {
        List<String> entries = new java.util.ArrayList<>();
        entries.add(note);
        entries.addAll(additionalReports);
        return entries;
    }
}
