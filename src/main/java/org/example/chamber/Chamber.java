package org.example.chamber;

import org.example.crop.CropProfile;
import org.example.devices.ControlTarget;
import org.example.devices.ControlType;

/** One growing chamber: an id, a crop profile, its live state and its summary. */
public final class Chamber implements ControlTarget {
    private final String id;
    private final CropProfile crop;
    private final ChamberState state = new ChamberState();
    private final ChamberSummary summary = new ChamberSummary();

    public Chamber(String id, CropProfile crop) {
        this.id = id;
        this.crop = crop;
    }

    public String id() { return id; }
    public CropProfile crop() { return crop; }
    public ChamberState state() { return state; }
    public ChamberSummary summary() { return summary; }

    @Override
    public void apply(ControlType control) {
        double value = state.apply(control);
        summary.recordAction(control.label() + " Activated");
        System.out.println("\t\u001B[32m[" + id + "][CONTROL] " + control.label() + " activated -> "
                + control.target().displayName() + " now " + value + control.target().unit() + "\u001B[0m");
    }
}
