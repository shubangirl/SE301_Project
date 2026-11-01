package org.example.crop;

import java.util.Collection;

/**
 * Supplies crop profiles to a {@link CropRegistry}. This is the extension point for adding crops:
 * implement it (in code, from a config file, from a database) and load it. Nothing existing changes.
 *
 * <p>Implementations may also be discovered with {@link java.util.ServiceLoader} by listing them in
 * {@code META-INF/services/org.example.crop.CropSource}.
 */
public interface CropSource {
    Collection<CropProfile> crops();
}
