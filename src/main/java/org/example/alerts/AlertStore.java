package org.example.alerts;

import java.util.List;

/** Storage abstraction for alerts, so AlertService doesn't care whether they live in memory or a database. */
public interface AlertStore {
    void add(Alert alert);
    List<Alert> getActiveAlerts();
    List<Alert> archiveAll();
    List<Alert> getArchivedAlerts();
    int getActiveCount();
    int getArchivedCount();
}
