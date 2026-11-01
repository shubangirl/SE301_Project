package org.example.reports;

import org.example.alerts.Alert;

import java.util.List;

/** Everything the daily report needs, replacing an eight-argument method. */
public record DailyReportData(
        int generatedAlerts,
        int activeAlerts,
        int archivedAlertCount,
        int waterUsage,
        int energyUsage,
        List<String> reportEntries,
        List<String> chamberSummaries,
        List<Alert> archivedAlerts
) {}
