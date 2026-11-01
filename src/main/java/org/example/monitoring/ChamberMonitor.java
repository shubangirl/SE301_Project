package org.example.monitoring;

import org.example.alerts.AlertPublisher;
import org.example.alerts.AlertSeverity;
import org.example.chamber.Chamber;
import org.example.crop.Rule;
import org.example.devices.ControlDispatcher;
import org.example.model.SensorType;

import java.util.Optional;
import java.util.Random;

/** Producer task for one chamber: reads sensors, checks the crop's rules, raises alerts, requests devices. */
public final class ChamberMonitor implements Runnable {
    private static final int CYCLES = 20;

    private final Chamber chamber;
    private final AlertPublisher alerts;
    private final ResourceUsage usage;
    private final ControlDispatcher controls;

    public ChamberMonitor(Chamber chamber, AlertPublisher alerts, ResourceUsage usage, ControlDispatcher controls) {
        this.chamber = chamber;
        this.alerts = alerts;
        this.usage = usage;
        this.controls = controls;
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            for (int i = 0; i < CYCLES; i++) {
                for (SensorType sensor : SensorType.values()) {
                    process(sensor, sensor.simulate(random));
                }
                usage.add(2, 1); // baseline consumption per cycle
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Package-visible so tests can feed a reading directly. */
    void process(SensorType sensor, double value) throws InterruptedException {
        if (!Double.isFinite(value)) {
            alerts.createSensorFailureAlert(
                    chamber.id(),
                    chamber.crop().name(),
                    sensor.name(),
                    "invalid reading"
            );
            chamber.summary().recordAlert("Sensor failure: " + sensor.name());
            return;
        }

        chamber.state().set(sensor, value);

        for (Rule rule : chamber.crop().rulesFor(sensor)) {
            Optional<Rule.Breach> breach = rule.check(value);
            if (breach.isEmpty()) {
                continue;
            }

            Rule.Action action = breach.get().action();
            String label = breach.get().high() ? sensor.highLabel() : sensor.lowLabel();
            String message = chamber.id() + " " + chamber.crop().name() + " " + label;

            alerts.createAlert(
                    chamber.id(),
                    chamber.crop().name(),
                    message,
                    AlertSeverity.WARNING
            );
            chamber.summary().recordAlert(message);
            usage.add(action.water(), action.energy());
            usage.addNutrient(action.nutrient());

            if (action.control() != null) {
                controls.submit(chamber, action.control());
            }
        }
    }
}
