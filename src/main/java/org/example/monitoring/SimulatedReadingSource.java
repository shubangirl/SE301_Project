package org.example.monitoring;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import org.example.model.SensorType;

/**
 * Fake readings for the simulation (same ranges as before: min +
 * nextInt(span)).
 */
public final class SimulatedReadingSource implements ReadingSource {
    public record Range(int min, int span) {
    }

    private final Random random;
    private final Map<SensorType, Range> ranges;

    public SimulatedReadingSource(Random random) {
        this(random, defaultRanges());
    }

    public SimulatedReadingSource(Random random, Map<SensorType, Range> ranges) {
        this.random = random;
        this.ranges = new EnumMap<>(ranges);
    }

    @Override
    public double read(SensorType sensor) {
        Range range = ranges.get(sensor);
        if (range == null) {
            throw new IllegalStateException("No simulation range for " + sensor);
        }
        return range.min() + random.nextInt(range.span());
    }

    private static Map<SensorType, Range> defaultRanges() {
        Map<SensorType, Range> ranges = new EnumMap<>(SensorType.class);
        ranges.put(SensorType.TEMPERATURE, new Range(10, 30));
        ranges.put(SensorType.HUMIDITY, new Range(40, 60));
        ranges.put(SensorType.SOIL, new Range(20, 60));
        ranges.put(SensorType.LIGHT, new Range(300, 900));
        ranges.put(SensorType.NUTRIENT, new Range(20, 80));
        return ranges;
    }
}
