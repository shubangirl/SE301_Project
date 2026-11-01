package org.example.chamber;

import org.example.devices.ControlDevice;

/** Where a chamber reports that a device ran. Console, file or test double: Chamber doesn't care. */
public interface ActionReporter {
    void deviceActivated(String chamberId, ControlDevice device, double resultingValue);
}
