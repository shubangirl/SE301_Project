package org.example;

import org.example.alerts.AlertService;
import org.example.chamber.Chamber;
import org.example.monitoring.ResourceUsage;
import org.example.reports.ReportService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Gathers results from the finished run and asks {@link ReportService} to format them. */
public final class ReportAssembler {
    private final ReportService reports;
    private final AlertService alerts;
    private final ResourceUsage usage;

    public ReportAssembler(ReportService reports, AlertService alerts, ResourceUsage usage) {
        this.reports = reports;
        this.alerts = alerts;
        this.usage = usage;
    }

    /** @param archivedThisRun how many alerts were moved to the archive just before reporting */
    public List<String> dailyReport(List<Chamber> chambers, int archivedThisRun) {
        List<String> chamberLines = chambers.stream()
                .sorted(Comparator.comparing(Chamber::id))
                .map(c -> c.id() + " -> " + c.summary().describe())
                .toList();

        List<String> entries = new ArrayList<>();
        entries.add(archivedThisRun > 0 ? "Archived " + archivedThisRun + " alerts" : "No alerts to archive");
        entries.addAll(reports.createWeeklyCropReport(chamberLines));
        entries.addAll(reports.createWaterUsageReport(usage.water()));
        entries.addAll(reports.createEnergyConsumptionReport(usage.energy()));
        entries.addAll(reports.createNutrientConsumptionReport(usage.nutrient()));

        var archived = alerts.getArchivedAlerts();
        return reports.createDailyReport(
                alerts.getTotalAlertsGenerated(),
                alerts.getActiveAlertCount(),
                archived.size(),
                usage.water(),
                usage.energy(),
                entries,
                chamberLines,
                archived);
    }
}
