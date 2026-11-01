package org.example.alerts;

import java.time.Instant;

public final class Alert {
    private final String chamber;
    private final String crop;
    private final String message;
    private final AlertSeverity severity;
    private final Instant timestamp;

    public Alert(
            String chamber,
            String crop,
            String message,
            AlertSeverity severity
    ) {
        this.chamber = chamber;
        this.crop = crop;
        this.message = message;
        this.severity = severity;
        this.timestamp = Instant.now();
    }

    public String getChamber() {
        return chamber;
    }

    public String getCrop() {
        return crop;
    }

    public String getMessage() {
        return message;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + severity + "][" + chamber + "][" + crop + "] " + message;
    }
}