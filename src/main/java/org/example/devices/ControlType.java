package org.example.devices;

import org.example.model.SensorType;

import static org.example.model.SensorType.*;

/**
 * Catalog of the built-in devices. It only holds data and delegates behaviour to {@link Adjustment}.
 * Code elsewhere should accept {@link ControlDevice}, so extra devices can be added without editing this enum.
 */
public enum ControlType implements ControlDevice {
    COOLING_FAN       ("Cooling Fan",        TEMPERATURE, Adjustment.lower(5, 18)),
    HEATING_UNIT      ("Heating Unit",       TEMPERATURE, Adjustment.raise(5, 30)),
    WATER_PUMP        ("Water Pump",         SOIL,        Adjustment.raise(20, 100)),
    NUTRIENT_INJECTOR ("Nutrient Injector",  NUTRIENT,    Adjustment.raise(15, 100)),
    HUMIDIFIER        ("Humidifier",         HUMIDITY,    Adjustment.raise(10, 100)),
    DEHUMIDIFIER      ("Dehumidifier",       HUMIDITY,    Adjustment.lower(10, 30)),
    VENTILATION_SYSTEM("Ventilation System", HUMIDITY,    Adjustment.lower(5, 30)),
    GROW_LIGHT        ("Grow Light",         LIGHT,       Adjustment.raise(150, 1000)),
    SHADE_CONTROLLER  ("Shade Controller",   LIGHT,       Adjustment.lower(150, 0));

    private final String label;
    private final SensorType target;
    private final Adjustment adjustment;

    ControlType(String label, SensorType target, Adjustment adjustment) {
        this.label = label;
        this.target = target;
        this.adjustment = adjustment;
    }

    @Override public String label() { return label; }
    @Override public SensorType target() { return target; }
    @Override public double next(double current) { return adjustment.apply(current); }
}
