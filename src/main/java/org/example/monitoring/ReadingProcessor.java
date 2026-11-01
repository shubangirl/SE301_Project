package org.example.monitoring;

import org.example.alerts.AlertPublisher;
import org.example.alerts.AlertSeverity;
import org.example.chamber.Chamber;
import org.example.crop.Rule;
import org.example.devices.ControlDispatcher;
import org.example.model.SensorType;

import java.util.Optional;

/** Decides what a single sensor reading means: record it, check the crop's rules, alert, account, dispatch. */
public final class ReadingProcessor {
    private final Chamber chamber;
    private final AlertPublisher alerts;
    private final UsageRecorder usage;
    private final ControlDispatcher controls;

    public ReadingProcessor(Chamber chamber, AlertPublisher alerts, UsageRecorder usage, ControlDispatcher controls) {
        this.chamber = chamber;
        this.alerts = alerts;
        this.usage = usage;
        this.controls = controls;
    }

    public void process(SensorType sensor, double value) throws InterruptedException {
        if (!Double.isFinite(value)) {
            reportSensorFailure(sensor);
            return;
        }
        chamber.state().set(sensor, value);
        for (Rule rule : chamber.crop().rulesFor(sensor)) {
            Optional<Rule.Breach> breach = rule.check(value);
            if (breach.isPresent()) {
                handleBreach(sensor, breach.get());
            }
        }
    }

    private void reportSensorFailure(SensorType sensor) {
        alerts.createSensorFailureAlert(chamber.id(), chamber.crop().name(), sensor.name(), "invalid reading");
        chamber.summary().recordAlert("Sensor failure: " + sensor.name());
    }

    private void handleBreach(SensorType sensor, Rule.Breach breach) throws InterruptedException {
        Rule.Action action = breach.action();
        String label = breach.high() ? sensor.highLabel() : sensor.lowLabel();
        String message = chamber.id() + " " + chamber.crop().name() + " " + label;

        alerts.createAlert(chamber.id(), chamber.crop().name(), message, AlertSeverity.WARNING);
        chamber.summary().recordAlert(message);
        usage.add(action.water(), action.energy());
        usage.addNutrient(action.nutrient());

        if (action.control() != null) {
            controls.submit(chamber, action.control());
        }
    }
}
