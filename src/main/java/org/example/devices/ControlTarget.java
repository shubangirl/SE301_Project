package org.example.devices;

/** Anything a device can act on (a chamber). */
public interface ControlTarget {
    void apply(ControlDevice device);
}
