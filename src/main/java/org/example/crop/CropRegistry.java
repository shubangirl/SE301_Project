package org.example.crop;

import org.example.crop.Rule.Action;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.devices.ControlType.*;
import static org.example.model.SensorType.*;

/** Registry of known crops (Factory/Registry). Crops can be added or removed at runtime without touching other code. */
public final class CropRegistry {
    private final Map<String, CropProfile> profiles = new ConcurrentHashMap<>();

    public void register(CropProfile profile) {
        profiles.put(key(profile.name()), profile);
    }

    public boolean remove(String name) {
        return profiles.remove(key(name)) != null;
    }

    public CropProfile get(String name) {
        CropProfile profile = profiles.get(key(name));
        if (profile == null) {
            throw new IllegalArgumentException("Unknown crop: " + name);
        }
        return profile;
    }

    public Collection<CropProfile> all() {
        return profiles.values();
    }

    private static String key(String name) {
        return name.toLowerCase();
    }

    /** Legacy crops (thresholds copied from the original SVFRMS) plus Bell Pepper, the new crop. */
    public static CropRegistry withDefaults() {
        CropRegistry registry = new CropRegistry();
        registry.register(CropProfile.of("Tomato",
                new Rule(TEMPERATURE, 22, 28, Action.of(null, 0, 15), Action.of(COOLING_FAN, 0, 10)),
                new Rule(HUMIDITY, 60, 80, Action.of(HUMIDIFIER, 20, 0), Action.of(DEHUMIDIFIER, 0, 0)),
                Rule.lowOnly(SOIL, 45, Action.of(WATER_PUMP, 50, 0))));
        registry.register(CropProfile.of("Strawberry",
                new Rule(TEMPERATURE, 18, 24, Action.of(null, 0, 15), Action.of(COOLING_FAN, 0, 10)),
                new Rule(HUMIDITY, 70, 85, Action.of(HUMIDIFIER, 25, 0), Action.of(DEHUMIDIFIER, 0, 0))));
        registry.register(CropProfile.of("Lettuce", Rule.temperature(15, 21)));
        registry.register(CropProfile.of("Spinach", Rule.temperature(10, 20)));
        registry.register(CropProfile.of("Chili", Rule.temperature(24, 32)));
        registry.register(CropProfile.of("Cucumber", Rule.temperature(18, 27)));
        // New crop: needs only this entry; no existing class was modified to support it.
        registry.register(CropProfile.of("Bell Pepper",
                Rule.temperature(20, 28),
                new Rule(HUMIDITY, 50, 70, Action.of(HUMIDIFIER, 15, 0), Action.of(DEHUMIDIFIER, 0, 0)),
                Rule.lowOnly(SOIL, 50, Action.of(WATER_PUMP, 40, 0))));
        return registry;
    }
}
