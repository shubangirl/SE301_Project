package org.example.crop;

import org.example.devices.ControlDevice;
import org.example.devices.ControlType;
import org.example.model.SensorType;

import java.util.Objects;
import java.util.Optional;

/** One threshold rule: what to do when a sensor goes below {@code low} or above {@code high}. */
public record Rule(SensorType sensor, double low, double high, Action onLow, Action onHigh) {

    public Rule {
        Objects.requireNonNull(sensor, "sensor");
        Objects.requireNonNull(onLow, "onLow (use Action.NONE for no reaction)");
        Objects.requireNonNull(onHigh, "onHigh (use Action.NONE for no reaction)");
        if (Double.isNaN(low) || Double.isNaN(high) || low > high) {
            throw new IllegalArgumentException(
                    "Invalid range for " + sensor + ": low=" + low + ", high=" + high);
        }
    }

    /** Reaction to a breach: optional device to trigger plus resource cost. Any {@link ControlDevice} works. */
    public record Action(ControlDevice control, int water, int energy, int nutrient) {
        public static final Action NONE = new Action(null, 0, 0, 0);

        public static Action of(ControlDevice control, int water, int energy) {
            return new Action(control, water, energy, 0);
        }

        public static Action of(ControlDevice control, int water, int energy, int nutrient) {
            return new Action(control, water, energy, nutrient);
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

    /** Temperature band using the standard response: heater when too cold, cooling fan when too hot. */
    public static Rule temperature(double low, double high) {
        return temperature(low, high,
                Action.of(ControlType.HEATING_UNIT, 0, 15),
                Action.of(ControlType.COOLING_FAN, 0, 0));
    }

    /** Temperature band with a custom response, so a crop is never forced into the default one. */
    public static Rule temperature(double low, double high, Action onLow, Action onHigh) {
        return new Rule(SensorType.TEMPERATURE, low, high, onLow, onHigh);
    }
}
