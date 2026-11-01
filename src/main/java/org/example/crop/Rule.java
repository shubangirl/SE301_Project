package org.example.crop;

import org.example.devices.ControlType;
import org.example.model.SensorType;

import java.util.Optional;

/** One threshold rule: what to do when a sensor goes below {@code low} or above {@code high}. */
public record Rule(SensorType sensor, double low, double high, Action onLow, Action onHigh) {

    /** Reaction to a breach: optional device to trigger plus resource cost. */
    public record Action(ControlType control, int water, int energy) {
        public static final Action NONE = new Action(null, 0, 0);

        public static Action of(ControlType control, int water, int energy) {
            return new Action(control, water, energy);
        }
    }

    public record Breach(boolean high, Action action) {}

    public Optional<Breach> check(double value) {
        if (value > high) {
            return Optional.of(new Breach(true, onHigh));
        }
        if (value < low) {
            return Optional.of(new Breach(false, onLow));
        }
        return Optional.empty();
    }

    public static Rule lowOnly(SensorType sensor, double low, Action onLow) {
        return new Rule(sensor, low, Double.POSITIVE_INFINITY, onLow, Action.NONE);
    }

    /** Temperature band where overheating triggers the cooling fan. */
    public static Rule temperature(double low, double high) {
        return new Rule(SensorType.TEMPERATURE, low, high, Action.NONE,
                Action.of(ControlType.COOLING_FAN, 0, 0));
    }
}
