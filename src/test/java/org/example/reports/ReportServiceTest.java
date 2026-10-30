package org.example.reports;

import org.example.alerts.Alert;
import org.example.alerts.AlertSeverity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportServiceTest {
    @Test
    void includesOperationalTotalsAndArchivedAlerts() {
        Alert archivedAlert = new Alert(
                "CH1",
                "Tomato",
                "High temperature",
                AlertSeverity.WARNING
        );

        List<String> report = new ReportService().createDailyReport(
                3,
                1,
                2,
                100,
                20,
                List.of("Daily Report"),
                List.of("CH1 -> Alerts=1"),
                List.of(archivedAlert)
        );

        assertTrue(report.contains("Generated Alerts: 3"));
        assertTrue(report.contains("Active Alerts: 1"));
        assertTrue(report.contains("Water Usage: 100 L"));
        assertTrue(report.contains("Energy Usage: 20 kWh"));
        assertTrue(report.contains("[WARNING][CH1][Tomato] High temperature"));
    }
}
