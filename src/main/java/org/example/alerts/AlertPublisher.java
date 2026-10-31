package org.example.alerts;

/** What the monitoring layer needs from alerting: raise an alert. */
public interface AlertPublisher {
    Alert createAlert(String chamber, String crop, String message, AlertSeverity severity);
}
