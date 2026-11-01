package org.example.devices;

import org.example.model.SensorType;

/** General-purpose device: add new hardware by constructing one, without touching existing code. */
public record ConfigurableDevice(String label, SensorType target, Adjustment adjustment) implements ControlDevice {
    @Override
    public double next(double current) {
        return adjustment.apply(current);
    }
}
