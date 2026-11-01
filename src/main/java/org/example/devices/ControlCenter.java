package org.example.devices;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/** Consumer thread: takes queued requests and applies them to their targets. */
public final class ControlCenter implements ShutdownableDispatcher {
    private static final int DEFAULT_CAPACITY = 5;

    private sealed interface Message permits Command, Stop {}
    private record Command(ControlTarget target, ControlDevice device) implements Message {}
    private record Stop() implements Message {}

    private final BlockingQueue<Message> queue;

    public ControlCenter() {
        this(DEFAULT_CAPACITY);
    }

    public ControlCenter(int capacity) {
        this(new ArrayBlockingQueue<>(capacity));
    }

    private ControlCenter(BlockingQueue<Message> queue) {
        this.queue = queue;
    }

    @Override
    public void submit(ControlTarget target, ControlDevice device) throws InterruptedException {
        queue.put(new Command(target, device));
    }

    @Override
    public void shutdown() throws InterruptedException {
        queue.put(new Stop());
    }

    @Override
    public void run() {
        try {
            while (queue.take() instanceof Command command) {
                command.target().apply(command.device());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
