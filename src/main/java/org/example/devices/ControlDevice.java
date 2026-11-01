package org.example.devices;

import org.example.model.SensorType;

/** Abstraction for any automated device. Consumers depend on this, never on a concrete device list. */
public interface ControlDevice {
    String label();
    SensorType target();
    /** New sensor value after this device runs. */
    double next(double current);
}
