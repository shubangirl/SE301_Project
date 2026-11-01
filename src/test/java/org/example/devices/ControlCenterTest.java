package org.example.devices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ControlCenterTest {
    @Test
    void coolingFanNeverRaisesTemperatureAndRespectsLimit() {
        assertEquals(18, ControlType.COOLING_FAN.next(20));
        assertEquals(15, ControlType.COOLING_FAN.next(15));
        assertEquals(25, ControlType.COOLING_FAN.next(30));
    }

    @Test
    void supportsRequiredAdditionalDevices() {
        assertEquals(30, ControlType.HEATING_UNIT.next(25));
        assertEquals(30, ControlType.VENTILATION_SYSTEM.next(35));
        assertEquals(850, ControlType.SHADE_CONTROLLER.next(1000));
    }

    @Test
    void processesEveryQueuedRequestBeforeStopping() throws Exception {
        List<ControlDevice> applied = Collections.synchronizedList(new ArrayList<>());
        ControlTarget target = applied::add;
        ControlCenter center = new ControlCenter();
        Thread consumer = new Thread(center);
        consumer.start();
        int requests = 50; // more than the queue capacity, so producers must block safely
        for (int i = 0; i < requests; i++) {
            center.submit(target, ControlType.WATER_PUMP);
        }
        center.shutdown();
        consumer.join();

        assertEquals(requests, applied.size());
    }
}
