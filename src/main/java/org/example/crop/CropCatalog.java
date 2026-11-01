package org.example.crop;

import java.util.Collection;
import java.util.Optional;

/** Read-only view of known crops. Code that only looks crops up should depend on this, not on the registry. */
public interface CropCatalog {
    Optional<CropProfile> find(String name);
    CropProfile get(String name);
    Collection<CropProfile> all();
}
