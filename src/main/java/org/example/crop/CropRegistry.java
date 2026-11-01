package org.example.crop;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry of known crops. Crops can be added or removed at runtime, and whole sets of crops can be
 * loaded from any {@link CropSource}, without touching this class.
 */
public final class CropRegistry implements CropCatalog {
    private final Map<String, CropProfile> profiles = new ConcurrentHashMap<>();

    public void register(CropProfile profile) {
        Objects.requireNonNull(profile, "profile");
        profiles.put(key(profile.name()), profile);
    }

    /** Registers every crop supplied by the source. Later registrations replace earlier ones of the same name. */
    public CropRegistry registerAll(CropSource source) {
        Objects.requireNonNull(source, "source").crops().forEach(this::register);
        return this;
    }

    public boolean remove(String name) {
        return profiles.remove(key(name)) != null;
    }

    @Override
    public Optional<CropProfile> find(String name) {
        return Optional.ofNullable(profiles.get(key(name)));
    }

    @Override
    public CropProfile get(String name) {
        return find(name).orElseThrow(() -> new IllegalArgumentException("Unknown crop: " + name));
    }

    @Override
    public Collection<CropProfile> all() {
        return Collections.unmodifiableCollection(profiles.values());
    }

    private static String key(String name) {
        return Objects.requireNonNull(name, "name").strip().toLowerCase(Locale.ROOT);
    }

    /** Registry preloaded with the built-in crops. */
    public static CropRegistry withDefaults() {
        return new CropRegistry().registerAll(new DefaultCrops());
    }

    /** Registry preloaded with the built-in crops plus every {@link CropSource} found on the classpath. */
    public static CropRegistry withDiscoveredSources() {
        CropRegistry registry = withDefaults();
        ServiceLoader.load(CropSource.class).forEach(registry::registerAll);
        return registry;
    }
}
