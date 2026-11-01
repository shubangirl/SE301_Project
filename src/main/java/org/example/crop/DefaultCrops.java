package org.example.crop;

import org.example.crop.Rule.Action;

import java.util.List;

import static org.example.devices.ControlType.*;
import static org.example.model.SensorType.*;

/** Built-in crops: legacy thresholds from the original SVFRMS plus Bell Pepper. Pure data, no behaviour. */
public final class DefaultCrops implements CropSource {

    /** Temperature response used by Tomato and Strawberry: heater when cold, fan (costing 10 kWh) when hot. */
    private static final Action HEAT = Action.of(HEATING_UNIT, 0, 15);
    private static final Action COOL = Action.of(COOLING_FAN, 0, 10);

    @Override
    public List<CropProfile> crops() {
        return List.of(
                CropProfile.of("Tomato",
                        Rule.temperature(22, 28, HEAT, COOL),
                        humidity(60, 80, Action.of(HUMIDIFIER, 20, 0), Action.of(DEHUMIDIFIER, 0, 0)),
                        Rule.lowOnly(SOIL, 45, Action.of(WATER_PUMP, 50, 0))),
                CropProfile.of("Strawberry",
                        Rule.temperature(18, 24, HEAT, COOL),
                        humidity(70, 85, Action.of(HUMIDIFIER, 25, 0), Action.of(DEHUMIDIFIER, 0, 0))),
                CropProfile.of("Lettuce", Rule.temperature(15, 21)),
                CropProfile.of("Spinach", Rule.temperature(10, 20)),
                CropProfile.of("Chili", Rule.temperature(24, 32)),
                CropProfile.of("Cucumber", Rule.temperature(18, 27)),
                CropProfile.of("Bell Pepper",
                        Rule.temperature(20, 28),
                        humidity(50, 70, Action.of(HUMIDIFIER, 15, 0), Action.of(VENTILATION_SYSTEM, 0, 0)),
                        Rule.lowOnly(SOIL, 50, Action.of(WATER_PUMP, 40, 0)))
        // Kale demo: uncomment these four lines
                // , CropProfile.of("Kale",
                //         Rule.temperature(15, 22),
                //         humidity(55, 75, Action.of(HUMIDIFIER, 15, 0), Action.of(VENTILATION_SYSTEM, 0, 0)),
                //         Rule.lowOnly(SOIL, 45, Action.of(WATER_PUMP, 40, 0)))
        );
    }

    private static Rule humidity(double low, double high, Action onLow, Action onHigh) {
        return new Rule(HUMIDITY, low, high, onLow, onHigh);
    }
}
