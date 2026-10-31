package org.example.devices;

/** What producers (chamber monitors) need from the device layer: just a way to request an action. */
public interface ControlDispatcher {
    void submit(ControlTarget target, ControlType control) throws InterruptedException;
}
