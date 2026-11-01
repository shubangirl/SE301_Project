package org.example.crop;

import org.example.devices.ControlType;
import org.example.model.SensorType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CropRegistryTest {
    private final CropRegistry registry = CropRegistry.withDefaults();

    @Test
    void tomatoTooHotBreachesHighAndRequestsCoolingFan() {
        Rule rule = registry.get("Tomato").rulesFor(SensorType.TEMPERATURE).getFirst();
        Rule.Breach breach = rule.check(29).orElseThrow();
        assertTrue(breach.high());
        assertEquals(ControlType.COOLING_FAN, breach.action().control());
        assertEquals(10, breach.action().energy());
    }

    @Test
    void valueInsideRangeIsNotABreach() {
        assertTrue(registry.get("Tomato").rulesFor(SensorType.TEMPERATURE).getFirst().check(25).isEmpty());
    }

    @Test
    void lettuceHasNoHumidityRuleLikeLegacyCode() {
        assertTrue(registry.get("Lettuce").rulesFor(SensorType.HUMIDITY).isEmpty());
    }

    @Test
    void everyCropHasSharedLightAndNutrientRules() {
        for (CropProfile crop : registry.all()) {
            assertEquals(1, crop.rulesFor(SensorType.LIGHT).size(), crop.name());
            assertEquals(1, crop.rulesFor(SensorType.NUTRIENT).size(), crop.name());
        }
    }

    @Test
    void newCropCanBeRegisteredAndRemovedWithoutChangingExistingCode() {
        registry.register(CropProfile.of("Kale", Rule.temperature(10, 24)));
        assertEquals("Kale", registry.get("kale").name());
        assertTrue(registry.remove("Kale"));
        assertThrows(IllegalArgumentException.class, () -> registry.get("Kale"));
    }

    @Test
    void bellPepperIsRegistered() {
        assertEquals("Bell Pepper", registry.get("bell pepper").name());
    }

    @Test
    void temperatureLowTriggersHeatingAndLightHighTriggersShade() {
        Rule temperature = registry.get("Tomato")
                .rulesFor(SensorType.TEMPERATURE).getFirst();
        Rule light = registry.get("Tomato")
                .rulesFor(SensorType.LIGHT).getFirst();

        assertEquals(ControlType.HEATING_UNIT, temperature.check(20).orElseThrow().action().control());
        assertEquals(ControlType.SHADE_CONTROLLER, light.check(1000).orElseThrow().action().control());
    }

    @Test
    void bellPepperHighHumidityTriggersVentilation() {
        Rule humidity = registry.get("Bell Pepper")
                .rulesFor(SensorType.HUMIDITY).getFirst();

        assertEquals(
                ControlType.VENTILATION_SYSTEM,
                humidity.check(75).orElseThrow().action().control()
        );
    }
}
