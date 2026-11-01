package org.example.monitoring;

import org.example.alerts.AlertRepository;
import org.example.alerts.AlertService;
import org.example.chamber.Chamber;
import org.example.crop.CropRegistry;
import org.example.devices.ControlType;
import org.example.model.SensorType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChamberMonitorTest {
    @Test
    void hotTomatoRaisesAlertChargesEnergyAndRequestsFan() throws Exception {
        AlertService alerts = new AlertService(new AlertRepository());
        ResourceUsage usage = new ResourceUsage();
        List<ControlType> requested = new ArrayList<>();
        Chamber chamber = new Chamber("CH1", CropRegistry.withDefaults().get("Tomato"));
        ChamberMonitor monitor = new ChamberMonitor(chamber, alerts, usage,
                (target, control) -> requested.add(control));

        monitor.process(SensorType.TEMPERATURE, 30);

        assertEquals(1, alerts.getActiveAlertCount());
        assertEquals(10, usage.energy());
        assertEquals(List.of(ControlType.COOLING_FAN), requested);
        assertTrue(alerts.getActiveAlerts().getFirst().getMessage().contains("Tomato High Temp"));
    }

    @Test
    void readingInsideRangeDoesNothing() throws Exception {
        AlertService alerts = new AlertService(new AlertRepository());
        Chamber chamber = new Chamber("CH1", CropRegistry.withDefaults().get("Tomato"));
        new ChamberMonitor(chamber, alerts, new ResourceUsage(), (t, c) -> { }).process(SensorType.TEMPERATURE, 25);
        assertEquals(0, alerts.getActiveAlertCount());
    }

    @Test
    void lowNutrientUsesRuleDefinedConsumption() throws Exception {
        AlertService alerts = new AlertService(new AlertRepository());
        ResourceUsage usage = new ResourceUsage();
        Chamber chamber = new Chamber("CH1", CropRegistry.withDefaults().get("Tomato"));

        new ChamberMonitor(chamber, alerts, usage, (t, c) -> { })
                .process(SensorType.NUTRIENT, 20);

        assertEquals(15, usage.nutrient());
    }
}
