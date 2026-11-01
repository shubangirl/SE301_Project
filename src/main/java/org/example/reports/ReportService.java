package org.example.reports;

import org.example.alerts.Alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportService {
    public List<String> createWeeklyCropReport(List<String> chamberSummaries) {
        List<String> report = new ArrayList<>();
        report.add("===== WEEKLY CROP REPORT =====");
        report.addAll(chamberSummaries);
        return report;
    }

    public List<String> createWaterUsageReport(int waterUsage) {
        return List.of(
                "===== WATER USAGE REPORT =====",
                "Water Usage: " + waterUsage + " L"
        );
    }

    public List<String> createEnergyConsumptionReport(int energyUsage) {
        return List.of(
                "===== ENERGY CONSUMPTION REPORT =====",
                "Energy Usage: " + energyUsage + " kWh"
        );
    }

    public List<String> createNutrientConsumptionReport(int nutrientUsage) {
        return List.of(
                "===== NUTRIENT CONSUMPTION REPORT =====",
                "Nutrient Usage: " + nutrientUsage + " units"
        );
    }

    public List<String> createDailyReport(
            int generatedAlerts,
            int activeAlerts,
            int archivedAlertCount,
            int waterUsage,
            int energyUsage,
            List<String> reportEntries,
            List<String> chamberSummaries,
            List<Alert> archivedAlerts
    ) {
        List<String> reportLines = new ArrayList<>();
        reportLines.add("===== DAILY REPORT =====");
        reportLines.add("Timestamp: " + new Date());
        reportLines.add("Generated Alerts: " + generatedAlerts);
        reportLines.add("Active Alerts: " + activeAlerts);
        reportLines.add("Archived alerts: " + archivedAlertCount);
        reportLines.add("Water Usage: " + waterUsage + " L");
        reportLines.add("Energy Usage: " + energyUsage + " kWh");
        reportLines.add("---");
        reportLines.addAll(reportEntries);

        if (!chamberSummaries.isEmpty()) {
            reportLines.add("--- chamber wise summary ---");
            reportLines.addAll(chamberSummaries);
        }

        if (!archivedAlerts.isEmpty()) {
            reportLines.add("--- archived alerts ---");
            for (Alert alert : archivedAlerts) {
                reportLines.add(alert.toString());
            }
        }

        return reportLines;
    }
}
