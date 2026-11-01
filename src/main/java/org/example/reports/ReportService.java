package org.example.reports;

import org.example.alerts.Alert;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Builds report text. Writing it out is {@link ReportSink}'s job. */
public class ReportService {
    private final Clock clock;

    public ReportService() {
        this(Clock.systemDefaultZone());
    }

    /** Inject a fixed clock in tests to get a predictable timestamp. */
    public ReportService(Clock clock) {
        this.clock = clock;
    }

    public List<String> createWeeklyCropReport(List<String> chamberSummaries) {
        List<String> report = new ArrayList<>();
        report.add(header("WEEKLY CROP REPORT"));
        report.addAll(chamberSummaries);
        return report;
    }

    public List<String> createWaterUsageReport(int waterUsage) {
        return List.of(header("WATER USAGE REPORT"), waterLine(waterUsage));
    }

    public List<String> createEnergyConsumptionReport(int energyUsage) {
        return List.of(header("ENERGY CONSUMPTION REPORT"), energyLine(energyUsage));
    }

    public List<String> createNutrientConsumptionReport(int nutrientUsage) {
        return List.of(header("NUTRIENT CONSUMPTION REPORT"), "Nutrient Usage: " + nutrientUsage + " units");
    }

    public List<String> createDailyReport(DailyReportData data) {
        List<String> lines = new ArrayList<>();
        lines.add(header("DAILY REPORT"));
        lines.add("Timestamp: " + new Date(clock.millis()));
        lines.add("Generated Alerts: " + data.generatedAlerts());
        lines.add("Active Alerts: " + data.activeAlerts());
        lines.add("Archived alerts: " + data.archivedAlertCount());
        lines.add(waterLine(data.waterUsage()));
        lines.add(energyLine(data.energyUsage()));
        lines.add("---");
        lines.addAll(data.reportEntries());

        if (!data.chamberSummaries().isEmpty()) {
            lines.add("--- chamber wise summary ---");
            lines.addAll(data.chamberSummaries());
        }
        if (!data.archivedAlerts().isEmpty()) {
            lines.add("--- archived alerts ---");
            for (Alert alert : data.archivedAlerts()) {
                lines.add(alert.toString());
            }
        }
        return lines;
    }

    /** Original signature, kept so existing callers compile. */
    public List<String> createDailyReport(
            int generatedAlerts, int activeAlerts, int archivedAlertCount,
            int waterUsage, int energyUsage,
            List<String> reportEntries, List<String> chamberSummaries, List<Alert> archivedAlerts) {
        return createDailyReport(new DailyReportData(generatedAlerts, activeAlerts, archivedAlertCount,
                waterUsage, energyUsage, reportEntries, chamberSummaries, archivedAlerts));
    }

    private static String header(String title) {
        return "===== " + title + " =====";
    }

    private static String waterLine(int litres) {
        return "Water Usage: " + litres + " L";
    }

    private static String energyLine(int kwh) {
        return "Energy Usage: " + kwh + " kWh";
    }
}
