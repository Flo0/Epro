package com.gestankbratwurst.epro.holograms.implementations.nms;

import com.gestankbratwurst.epro.holograms.abstraction.Hologram;
import com.gestankbratwurst.epro.holograms.abstraction.HologramFactory;
import org.bukkit.Location;

public class NMSHologramFactory implements HologramFactory {
  @Override
  public Hologram createHologram(Location location, String hologramName) {
    return new NMSHologram(location, hologramName);
  }
}
