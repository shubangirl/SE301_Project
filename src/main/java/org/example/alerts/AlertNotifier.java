package org.example.alerts;

/** Where alert events are announced (console, log, email...). */
public interface AlertNotifier {
    void alertRaised(Alert alert);
    void alertsArchived(int count);
}
