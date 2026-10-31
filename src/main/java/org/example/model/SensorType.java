package org.example.model;

import java.util.Random;

/** Every kind of sensor reading, with the metadata that used to be scattered across if/else chains. */
public enum SensorType {
    TEMPERATURE("Temperature",    "High Temp",          "Low Temp",          "C",    24,  10, 30),
    HUMIDITY   ("Humidity",       "High Humidity",      "Low Humidity",      "%",    70,  40, 60),
    SOIL       ("Soil moisture",  "High Soil Moisture", "Low Soil Moisture", "%",    60,  20, 60),
    LIGHT      ("Light level",    "High Light Level",   "Low Light Level",   " lux", 600, 300, 900),
    NUTRIENT   ("Nutrient level", "High Nutrient Level", "Low Nutrient Level", "%",  70,  20, 80);

    private final String displayName;
    private final String highLabel;
    private final String lowLabel;
    private final String unit;
    private final double initialValue;
    private final int simMin;
    private final int simSpan;

    SensorType(String displayName, String highLabel, String lowLabel, String unit,
               double initialValue, int simMin, int simSpan) {
        this.displayName = displayName;
        this.highLabel = highLabel;
        this.lowLabel = lowLabel;
        this.unit = unit;
        this.initialValue = initialValue;
        this.simMin = simMin;
        this.simSpan = simSpan;
    }

    public String displayName() { return displayName; }
    public String highLabel() { return highLabel; }
    public String lowLabel() { return lowLabel; }
    public String unit() { return unit; }
    public double initialValue() { return initialValue; }

    /** Fake reading for the simulation (same ranges as the legacy ChamberThread). */
    public double simulate(Random random) {
        return simMin + random.nextInt(simSpan);
    }
}
