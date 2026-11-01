package org.example.devices;

/** Anything a device can act on (a chamber). Lets the devices package avoid depending on concrete chamber classes. */
public interface ControlTarget {
    void apply(ControlType control);
}
