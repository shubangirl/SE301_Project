package org.example.monitoring;

import org.example.model.SensorType;

/** Where sensor readings come from: a simulator today, real hardware tomorrow. */
public interface ReadingSource {
    double read(SensorType sensor);
}
