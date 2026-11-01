package org.example.monitoring;

/** Thread-safe water/energy counters. Uses synchronized methods (Wk 9) so += is no longer a lost-update race. */
public final class ResourceUsage {
    private int water;
    private int energy;

    public synchronized void add(int waterLitres, int energyKwh) {
        water += waterLitres;
        energy += energyKwh;
    }

    public synchronized int water() { return water; }
    public synchronized int energy() { return energy; }
}
