package org.example.devices;

/** What producers need: a way to request an action. */
public interface ControlDispatcher {
    void submit(ControlTarget target, ControlDevice device) throws InterruptedException;
}
