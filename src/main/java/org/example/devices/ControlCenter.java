package org.example.devices;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/** Consumer thread. Replaces the hand-rolled wait/notifyAll queue with a BlockingQueue and a stop marker. */
public final class ControlCenter implements ControlDispatcher, Runnable {
    private record Request(ControlTarget target, ControlType control) {}

    private static final int CAPACITY = 5;
    private static final Request STOP = new Request(null, null);

    private final BlockingQueue<Request> queue = new ArrayBlockingQueue<>(CAPACITY);

    /** Blocks while the queue is full (same bounded-buffer behaviour as the legacy code). */
    @Override
    public void submit(ControlTarget target, ControlType control) throws InterruptedException {
        queue.put(new Request(target, control));
    }

    /** Call once every producer has finished; requests already queued are still processed first. */
    public void shutdown() throws InterruptedException {
        queue.put(STOP);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request request = queue.take();
                if (request == STOP) {
                    return;
                }
                request.target().apply(request.control());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
