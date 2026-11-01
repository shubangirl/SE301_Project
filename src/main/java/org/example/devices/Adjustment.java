package org.example.devices;

/** Strategy: how a device moves a sensor value. New behaviours are new implementations, not edits. */
@FunctionalInterface
public interface Adjustment {
    double apply(double current);

    /** Increases the value by {@code amount}, never past {@code ceiling}, never lowering it. */
    static Adjustment raise(double amount, double ceiling) {
        return current -> current >= ceiling ? current : Math.min(ceiling, current + amount);
    }

    /** Decreases the value by {@code amount}, never below {@code floor}, never raising it. */
    static Adjustment lower(double amount, double floor) {
        return current -> current <= floor ? current : Math.max(floor, current - amount);
    }
}
