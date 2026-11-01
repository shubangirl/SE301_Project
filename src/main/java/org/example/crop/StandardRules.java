package org.example.crop;

import org.example.devices.ControlType;
import org.example.model.SensorType;

import java.util.LinkedHashMap;
import java.util.Map;

/** The shared default rules applied to every crop unless it overrides or opts out. Kept apart from CropProfile. */
final class StandardRules {
    static final double DEFAULT_MIN_LIGHT = 500;
    static final double DEFAULT_MAX_LIGHT = 900;
    static final double DEFAULT_MIN_NUTRIENT = 40;

    private StandardRules() {}

    /** Fresh, mutable map of sensor to default rule. Add an entry here to give every crop a new default. */
    static Map<SensorType, Rule> defaults() {
        Map<SensorType, Rule> defaults = new LinkedHashMap<>();
        defaults.put(SensorType.LIGHT, light(DEFAULT_MIN_LIGHT, DEFAULT_MAX_LIGHT));
        defaults.put(SensorType.NUTRIENT, nutrient(DEFAULT_MIN_NUTRIENT));
        return defaults;
    }

    static Rule light(double low, double high) {
        return new Rule(SensorType.LIGHT, low, high,
                Rule.Action.of(ControlType.GROW_LIGHT, 0, 0),
                Rule.Action.of(ControlType.SHADE_CONTROLLER, 0, 0));
    }

    static Rule nutrient(double low) {
        return Rule.lowOnly(SensorType.NUTRIENT, low,
                Rule.Action.of(ControlType.NUTRIENT_INJECTOR, 0, 0, 15));
    }
}
