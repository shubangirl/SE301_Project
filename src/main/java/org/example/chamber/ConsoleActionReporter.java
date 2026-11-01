package org.example.chamber;

import org.example.devices.ControlDevice;

/** Prints device activations to stdout in green. */
public final class ConsoleActionReporter implements ActionReporter {
    @Override
    public void deviceActivated(String chamberId, ControlDevice device, double resultingValue) {
        System.out.println("\t\u001B[32m[" + chamberId + "][CONTROL] " + device.label() + " activated -> "
                + device.target().displayName() + " now " + resultingValue + device.target().unit() + "\u001B[0m");
    }
}
