package org.example.devices;

import org.example.model.SensorType;

/** Automated devices. Each one nudges one sensor value toward a limit (enum replaces the old string if/else chain). */
public enum ControlType {
    COOLING_FAN      ("Cooling Fan",       SensorType.TEMPERATURE, -5,   18),
    HEATING_UNIT     ("Heating Unit",     SensorType.TEMPERATURE, +5,   30),
    WATER_PUMP       ("Water Pump",        SensorType.SOIL,        +20,  100),
    NUTRIENT_INJECTOR("Nutrient Injector", SensorType.NUTRIENT,    +15,  100),
    HUMIDIFIER       ("Humidifier",        SensorType.HUMIDITY,    +10,  100),
    DEHUMIDIFIER     ("Dehumidifier",      SensorType.HUMIDITY,    -10,  30),
    VENTILATION_SYSTEM("Ventilation System", SensorType.HUMIDITY, -5, 30),
    GROW_LIGHT       ("Grow Light",        SensorType.LIGHT,       +150, 1000),
    SHADE_CONTROLLER ("Shade Controller",  SensorType.LIGHT,       -150, 0);

    private final String label;
    private final SensorType target;
    private final double delta;
    private final double limit;

    ControlType(String label, SensorType target, double delta, double limit) {
        this.label = label;
        this.target = target;
        this.delta = delta;
        this.limit = limit;
    }

    public String label() { return label; }
    public SensorType target() { return target; }

    /** New value after running this device; never overshoots the limit and never pushes the wrong way. */
    public double next(double current) {
        if (delta < 0) {
            return current <= limit ? current : Math.max(limit, current + delta);
        }
        return current >= limit ? current : Math.min(limit, current + delta);
    }
}
