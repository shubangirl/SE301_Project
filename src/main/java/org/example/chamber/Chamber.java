package org.example.chamber;

import org.example.crop.CropProfile;
import org.example.devices.ControlDevice;
import org.example.devices.ControlTarget;

/** One growing chamber: ties together an id, a crop profile, its state and its summary. */
public final class Chamber implements ControlTarget {
    private final String id;
    private final CropProfile crop;
    private final ChamberState state;
    private final ChamberSummary summary;
    private final ActionReporter reporter;

    /** Default wiring, so existing callers keep working. */
    public Chamber(String id, CropProfile crop) {
        this(id, crop, new ChamberState(), new ChamberSummary(), new ConsoleActionReporter());
    }

    public Chamber(String id, CropProfile crop, ChamberState state,
                   ChamberSummary summary, ActionReporter reporter) {
        this.id = id;
        this.crop = crop;
        this.state = state;
        this.summary = summary;
        this.reporter = reporter;
    }

    public String id() { return id; }
    public CropProfile crop() { return crop; }
    public ChamberState state() { return state; }
    public ChamberSummary summary() { return summary; }

    @Override
    public void apply(ControlDevice device) {
        double value = state.apply(device);
        summary.recordAction(device.label() + " Activated");
        reporter.deviceActivated(id, device, value);
    }
}
