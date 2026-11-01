package org.example.alerts;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** Creates, counts and archives alerts. Storage and announcements are delegated to injected collaborators. */
public class AlertService implements AlertPublisher {
    private final AlertStore store;
    private final AlertNotifier notifier;
    private final AtomicInteger totalAlertsGenerated = new AtomicInteger();

    /** Default wiring (console output); existing callers passing an AlertRepository still work. */
    public AlertService(AlertStore store) {
        this(store, new ConsoleAlertNotifier());
    }

    public AlertService(AlertStore store, AlertNotifier notifier) {
        this.store = store;
        this.notifier = notifier;
    }

    @Override
    public Alert createAlert(String chamber, String crop, String message, AlertSeverity severity) {
        Alert alert = new Alert(chamber, crop, message, severity);
        store.add(alert);
        totalAlertsGenerated.incrementAndGet();
        notifier.alertRaised(alert);
        return alert;
    }

    @Override
    public Alert createSensorFailureAlert(String chamber, String crop, String sensor, String reason) {
        return createAlert(chamber, crop,
                "Sensor failure: " + sensor + " (" + reason + ")",
                AlertSeverity.CRITICAL);
    }

    public List<Alert> getActiveAlerts() {
        return store.getActiveAlerts();
    }

    public List<Alert> archiveAlerts() {
        List<Alert> archived = store.archiveAll();
        notifier.alertsArchived(archived.size());
        return archived;
    }

    public List<Alert> getArchivedAlerts() {
        return store.getArchivedAlerts();
    }

    public int getActiveAlertCount() {
        return store.getActiveCount();
    }

    public int getTotalAlertsGenerated() {
        return totalAlertsGenerated.get();
    }
}
