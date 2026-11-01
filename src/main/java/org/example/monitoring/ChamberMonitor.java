package org.example.monitoring;

import org.example.alerts.AlertPublisher;
import org.example.chamber.Chamber;
import org.example.devices.ControlDispatcher;
import org.example.model.SensorType;

import java.util.Random;

/** Producer task for one chamber: samples every sensor each cycle and hands readings to a {@link ReadingProcessor}. */
public final class ChamberMonitor implements Runnable {
    private static final int DEFAULT_CYCLES = 20;
    private static final int BASELINE_WATER = 2;
    private static final int BASELINE_ENERGY = 1;

    private final ReadingProcessor processor;
    private final ReadingSource readings;
    private final UsageRecorder usage;
    private final int cycles;

    /** Same signature as before, so existing callers keep working (uses simulated readings). */
    public ChamberMonitor(Chamber chamber, AlertPublisher alerts, UsageRecorder usage, ControlDispatcher controls) {
        this(new ReadingProcessor(chamber, alerts, usage, controls),
                new SimulatedReadingSource(new Random()), usage, DEFAULT_CYCLES);
    }

    public ChamberMonitor(ReadingProcessor processor, ReadingSource readings, UsageRecorder usage, int cycles) {
        this.processor = processor;
        this.readings = readings;
        this.usage = usage;
        this.cycles = cycles;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < cycles; i++) {
                for (SensorType sensor : SensorType.values()) {
                    process(sensor, readings.read(sensor));
                }
                usage.add(BASELINE_WATER, BASELINE_ENERGY);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Package-visible so tests can feed a reading directly. */
    void process(SensorType sensor, double value) throws InterruptedException {
        processor.process(sensor, value);
    }
}
