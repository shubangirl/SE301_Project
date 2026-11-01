package org.example.alerts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlertServiceTest {
    @Test
    void createsAndCountsAlert() {
        AlertService service = new AlertService(new AlertRepository());

        service.createAlert(
                "CH1",
                "Tomato",
                "High temperature",
                AlertSeverity.WARNING
        );

        assertEquals(1, service.getTotalAlertsGenerated());
        assertEquals(1, service.getActiveAlertCount());
        assertEquals("CH1", service.getActiveAlerts().getFirst().getChamber());
    }

    @Test
    void archivesActiveAlerts() {
        AlertService service = new AlertService(new AlertRepository());
        service.createAlert(
                "CH2",
                "Lettuce",
                "Low nutrient level",
                AlertSeverity.CRITICAL
        );

        assertEquals(1, service.archiveAlerts().size());
        assertEquals(0, service.getActiveAlertCount());
        assertEquals(1, service.getArchivedAlerts().size());
    }

    @Test
    void createsCriticalSensorFailureAlert() {
        AlertService service = new AlertService(new AlertRepository());

        Alert alert = service.createSensorFailureAlert(
                "CH1",
                "Tomato",
                "TEMPERATURE",
                "invalid reading"
        );

        assertEquals(AlertSeverity.CRITICAL, alert.getSeverity());
        assertEquals(1, service.getActiveAlertCount());
    }
}
