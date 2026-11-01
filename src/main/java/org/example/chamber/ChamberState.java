package org.example.chamber;

import org.example.devices.ControlDevice;
import org.example.model.SensorType;

import java.util.EnumMap;

/** Live sensor values for one chamber. Synchronized because monitor and control threads both touch it. */
public final class ChamberState {
    private final EnumMap<SensorType, Double> values = new EnumMap<>(SensorType.class);

    public ChamberState() {
        for (SensorType type : SensorType.values()) {
            values.put(type, type.initialValue());
        }
    }

    public synchronized void set(SensorType type, double value) {
        values.put(type, value);
    }

    public synchronized double get(SensorType type) {
        return values.get(type);
    }

    /** Runs a device against this state atomically and returns the resulting value. */
    public synchronized double apply(ControlDevice device) {
        double updated = device.next(values.get(device.target()));
        values.put(device.target(), updated);
        return updated;
    }
}
