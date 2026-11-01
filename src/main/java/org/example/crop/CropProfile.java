package org.example.crop;

import org.example.devices.ControlType;
import org.example.model.SensorType;

import java.util.ArrayList;
import java.util.List;

/** A crop's acceptable operating ranges, expressed as data (rules). No code change is needed per crop. */
public record CropProfile(String name, List<Rule> rules) {

    private static final double MIN_LIGHT = 500;
    private static final double MIN_NUTRIENT = 40;

    public CropProfile {
        rules = List.copyOf(rules);
    }

    /** Builds a profile from crop-specific rules plus the light and nutrient rules every crop shares. */
    public static CropProfile of(String name, Rule... specificRules) {
        List<Rule> all = new ArrayList<>(List.of(specificRules));
        all.add(new Rule(
                SensorType.LIGHT,
                MIN_LIGHT,
                900,
                Rule.Action.of(ControlType.GROW_LIGHT, 0, 0),
                Rule.Action.of(ControlType.SHADE_CONTROLLER, 0, 0)
        ));
        all.add(Rule.lowOnly(
                SensorType.NUTRIENT,
                MIN_NUTRIENT,
                Rule.Action.of(ControlType.NUTRIENT_INJECTOR, 0, 0, 15)
        ));
        return new CropProfile(name, all);
    }

    public List<Rule> rulesFor(SensorType sensor) {
        return rules.stream().filter(rule -> rule.sensor() == sensor).toList();
    }
}
