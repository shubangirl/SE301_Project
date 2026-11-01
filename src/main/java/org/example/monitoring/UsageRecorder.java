package org.example.monitoring;

/** Write side of resource tracking: all a monitor needs. Reporting code reads via {@link ResourceUsage}. */
public interface UsageRecorder {
    void add(int waterLitres, int energyKwh);
    void addNutrient(int amount);
}
