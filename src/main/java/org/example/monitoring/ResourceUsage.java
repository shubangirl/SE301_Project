package org.example.monitoring;

/** Thread-safe water/energy/nutrient counters. Synchronized methods avoid lost updates on +=. */
public final class ResourceUsage implements UsageRecorder {
    private int water;
    private int energy;
    private int nutrient;

    @Override
    public synchronized void add(int waterLitres, int energyKwh) {
        water += waterLitres;
        energy += energyKwh;
    }

    @Override
    public synchronized void addNutrient(int amount) {
        nutrient += amount;
    }

    public synchronized int water() { return water; }
    public synchronized int energy() { return energy; }
    public synchronized int nutrient() { return nutrient; }
}
