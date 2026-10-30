package org.example.reports;

import org.example.alerts.Alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportService {
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
