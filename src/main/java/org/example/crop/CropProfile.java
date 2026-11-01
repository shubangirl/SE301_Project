package org.example.crop;

import org.example.model.SensorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A crop's acceptable operating ranges, expressed as data (rules). No code change is needed per crop.
 *
 * <p>Shared default rules (see {@link StandardRules}) are added unless the crop supplies its own rule for
 * that sensor, replaces the default via the {@link Builder}, or opts out of it.
 */
public record CropProfile(String name, List<Rule> rules) {

    public CropProfile {
        Objects.requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Crop name must not be blank");
        }
        rules = List.copyOf(rules);
    }

    /** Builds a profile from crop-specific rules plus the shared defaults. */
    public static CropProfile of(String name, Rule... specificRules) {
        return builder(name).rules(specificRules).build();
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public List<Rule> rulesFor(SensorType sensor) {
        return rules.stream().filter(rule -> rule.sensor() == sensor).toList();
    }

    public static final class Builder {
        private final String name;
        private final List<Rule> specific = new ArrayList<>();
        private final Map<SensorType, Rule> defaults = StandardRules.defaults();

        private Builder(String name) {
            this.name = name;
        }

        public Builder rule(Rule rule) {
            specific.add(Objects.requireNonNull(rule, "rule"));
            return this;
        }

        public Builder rules(Rule... rules) {
            for (Rule rule : rules) {
                rule(rule);
            }
            return this;
        }

        /** Replaces the shared default for the rule's sensor (works for any sensor, not just light/nutrient). */
        public Builder defaultRule(Rule rule) {
            Objects.requireNonNull(rule, "rule");
            defaults.put(rule.sensor(), rule);
            return this;
        }

        /** Crop does not use the shared default for this sensor. */
        public Builder withoutDefault(SensorType sensor) {
            defaults.remove(sensor);
            return this;
        }

        /** Overrides the shared light band (e.g. a shade-loving crop). */
        public Builder light(double low, double high) {
            return defaultRule(StandardRules.light(low, high));
        }

        /** Overrides the shared minimum nutrient level. */
        public Builder nutrient(double low) {
            return defaultRule(StandardRules.nutrient(low));
        }

        /** Crop does not need light control. */
        public Builder noLightRule() {
            return withoutDefault(SensorType.LIGHT);
        }

        /** Crop does not need nutrient control. */
        public Builder noNutrientRule() {
            return withoutDefault(SensorType.NUTRIENT);
        }

        /** A crop-specific rule for a sensor always wins over the shared default, so no duplicates occur. */
        public CropProfile build() {
            List<Rule> all = new ArrayList<>(specific);
            defaults.values().forEach(shared -> addIfSensorAbsent(all, shared));
            return new CropProfile(name, all);
        }

        private static void addIfSensorAbsent(List<Rule> all, Rule shared) {
            if (all.stream().noneMatch(r -> r.sensor() == shared.sensor())) {
                all.add(shared);
            }
        }
    }
}
