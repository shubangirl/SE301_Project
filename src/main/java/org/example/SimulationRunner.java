package org.example;

import org.example.devices.ShutdownableDispatcher;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Runs the producer/consumer simulation: starts the control consumer, runs monitors, then shuts down in order. */
public final class SimulationRunner {
    private final ShutdownableDispatcher controls;
    private final Duration timeout;

    public SimulationRunner(ShutdownableDispatcher controls, Duration timeout) {
        this.controls = controls;
        this.timeout = timeout;
    }

    public void run(List<? extends Runnable> monitors) throws InterruptedException {
        Thread controlThread = new Thread(controls, "control-center");
        controlThread.start();

        ExecutorService pool = Executors.newFixedThreadPool(Math.max(1, monitors.size()));
        monitors.forEach(pool::submit);
        pool.shutdown();

        if (!pool.awaitTermination(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
            pool.shutdownNow();
            controlThread.interrupt(); // don't leave the consumer thread keeping the JVM alive
            throw new IllegalStateException("Chamber monitoring did not finish within " + timeout.toMinutes() + " minutes");
        }

        controls.shutdown();
        controlThread.join();
    }
}
