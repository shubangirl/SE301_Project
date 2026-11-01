package org.example.chamber;

/** Per-chamber alert/action tally for the report. */
public final class ChamberSummary {
    private int alerts;
    private int actions;
    private String lastAlert = "None";
    private String lastAction = "No action";

    public synchronized void recordAlert(String message) {
        alerts++;
        lastAlert = message;
    }

    public synchronized void recordAction(String message) {
        actions++;
        lastAction = message;
    }

    public synchronized String describe() {
        return "Alerts=" + alerts + ", Actions=" + actions
                + ", Last Action=" + lastAction + ", Last Alert=" + lastAlert;
    }
}
