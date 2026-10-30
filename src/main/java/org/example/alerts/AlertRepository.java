package org.example.alerts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlertRepository {
    private final List<Alert> activeAlerts = new CopyOnWriteArrayList<>();
    private final List<Alert> archivedAlerts = new CopyOnWriteArrayList<>();

    public void add(Alert alert) {
        activeAlerts.add(alert);
    }

    public List<Alert> getActiveAlerts() {
        return List.copyOf(activeAlerts);
    }

    public List<Alert> archiveAll() {
        List<Alert> alertsToArchive = new ArrayList<>(activeAlerts);

        if (!alertsToArchive.isEmpty()) {
            archivedAlerts.addAll(alertsToArchive);
            activeAlerts.removeAll(alertsToArchive);
        }

        return alertsToArchive;
    }

    public List<Alert> getArchivedAlerts() {
        return List.copyOf(archivedAlerts);
    }

    public int getActiveCount() {
        return activeAlerts.size();
    }

    public int getArchivedCount() {
        return archivedAlerts.size();
    }
}