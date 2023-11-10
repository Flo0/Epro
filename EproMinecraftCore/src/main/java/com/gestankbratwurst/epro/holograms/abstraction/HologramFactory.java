package com.gestankbratwurst.epro.holograms.abstraction;

import org.bukkit.Location;

public interface HologramFactory {

  Hologram createHologram(Location location, String hologramName);

}
