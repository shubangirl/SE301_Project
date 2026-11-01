package org.example.alerts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** In-memory {@link AlertStore}. */
public class AlertRepository implements AlertStore {
    private final List<Alert> activeAlerts = new CopyOnWriteArrayList<>();
    private final List<Alert> archivedAlerts = new CopyOnWriteArrayList<>();

    @Override
    public void add(Alert alert) {
        activeAlerts.add(alert);
    }

    @Override
    public List<Alert> getActiveAlerts() {
        return List.copyOf(activeAlerts);
    }

    /** Synchronized so two concurrent archive calls can't archive the same alert twice. */
    @Override
    public synchronized List<Alert> archiveAll() {
        List<Alert> alertsToArchive = new ArrayList<>(activeAlerts);

        if (!alertsToArchive.isEmpty()) {
            archivedAlerts.addAll(alertsToArchive);
            activeAlerts.removeAll(alertsToArchive);
        }

        return alertsToArchive;
    }

    @Override
    public List<Alert> getArchivedAlerts() {
        return List.copyOf(archivedAlerts);
    }

    @Override
    public int getActiveCount() {
        return activeAlerts.size();
    }

    @Override
    public int getArchivedCount() {
        return archivedAlerts.size();
    }
}
