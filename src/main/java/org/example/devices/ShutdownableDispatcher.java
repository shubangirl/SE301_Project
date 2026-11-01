package org.example.devices;

/** A dispatcher that also runs as the consumer thread and can be told to finish. */
public interface ShutdownableDispatcher extends ControlDispatcher, Runnable {
    /** Call once every producer has finished; requests already queued are still processed first. */
    void shutdown() throws InterruptedException;
}
