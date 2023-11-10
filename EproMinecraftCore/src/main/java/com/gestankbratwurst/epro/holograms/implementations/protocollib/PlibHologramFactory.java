package com.gestankbratwurst.epro.holograms.implementations.protocollib;


import com.gestankbratwurst.epro.holograms.abstraction.Hologram;
import com.gestankbratwurst.epro.holograms.abstraction.HologramFactory;
import org.bukkit.Location;

public class PlibHologramFactory implements HologramFactory {
  @Override
  public Hologram createHologram(Location location, String hologramName) {
    return new PlibHologram(location, hologramName);
  }
}
