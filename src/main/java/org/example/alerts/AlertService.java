package org.example.alerts;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AlertService implements AlertPublisher {
    private final AlertRepository repository;
    private final AtomicInteger totalAlertsGenerated = new AtomicInteger();

    public AlertService(AlertRepository repository) {
        this.repository = repository;
    }

    @Override
    public Alert createAlert(
            String chamber,
            String crop,
            String message,
            AlertSeverity severity
    ) {
        Alert alert = new Alert(chamber, crop, message, severity);

        repository.add(alert);
        totalAlertsGenerated.incrementAndGet();

        System.out.println(alert);
        return alert;
    }

    public Alert createSensorFailureAlert(
            String chamber,
            String crop,
            String sensor,
            String reason
    ) {
        return createAlert(
                chamber,
                crop,
                "Sensor failure: " + sensor + " (" + reason + ")",
                AlertSeverity.CRITICAL
        );
    }

    public List<Alert> getActiveAlerts() {
        return repository.getActiveAlerts();
    }

    public List<Alert> archiveAlerts() {
        List<Alert> archived = repository.archiveAll();

        if (archived.isEmpty()) {
            System.out.println("Archiving alerts... no active alerts to archive.");
        } else {
            System.out.println(
                    "Archiving alerts... "
                            + archived.size()
                            + " moved to archive."
            );
        }

        return archived;
    }

    public List<Alert> getArchivedAlerts() {
        return repository.getArchivedAlerts();
    }

    public int getActiveAlertCount() {
        return repository.getActiveCount();
    }

    public int getTotalAlertsGenerated() {
        return totalAlertsGenerated.get();
    }
}