package org.example.model;

/** Every kind of sensor reading, with its display metadata. Simulation lives elsewhere (see SimulatedReadingSource). */
public enum SensorType {
    TEMPERATURE("Temperature",    "High Temp",           "Low Temp",           "C",    24),
    HUMIDITY   ("Humidity",       "High Humidity",       "Low Humidity",       "%",    70),
    SOIL       ("Soil moisture",  "High Soil Moisture",  "Low Soil Moisture",  "%",    60),
    LIGHT      ("Light level",    "High Light Level",    "Low Light Level",    " lux", 600),
    NUTRIENT   ("Nutrient level", "High Nutrient Level", "Low Nutrient Level", "%",    70);

    private final String displayName;
    private final String highLabel;
    private final String lowLabel;
    private final String unit;
    private final double initialValue;

    SensorType(String displayName, String highLabel, String lowLabel, String unit, double initialValue) {
        this.displayName = displayName;
        this.highLabel = highLabel;
        this.lowLabel = lowLabel;
        this.unit = unit;
        this.initialValue = initialValue;
    }

    public String displayName() { return displayName; }
    public String highLabel() { return highLabel; }
    public String lowLabel() { return lowLabel; }
    public String unit() { return unit; }
    public double initialValue() { return initialValue; }
}
