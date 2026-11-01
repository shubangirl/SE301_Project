package org.example.alerts;

/** Prints alert events to stdout, exactly as AlertService used to. */
public final class ConsoleAlertNotifier implements AlertNotifier {
    @Override
    public void alertRaised(Alert alert) {
        System.out.println(alert);
    }

    @Override
    public void alertsArchived(int count) {
        if (count == 0) {
            System.out.println("Archiving alerts... no active alerts to archive.");
        } else {
            System.out.println("Archiving alerts... " + count + " moved to archive.");
        }
    }
}
