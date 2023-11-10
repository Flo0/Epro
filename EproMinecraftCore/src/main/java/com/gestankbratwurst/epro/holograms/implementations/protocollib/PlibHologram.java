package com.gestankbratwurst.epro.holograms.implementations.protocollib;

import com.gestankbratwurst.epro.holograms.abstraction.AbstractHologram;
import com.gestankbratwurst.epro.holograms.abstraction.HologramLine;
import org.bukkit.Location;

public class PlibHologram extends AbstractHologram {
  public PlibHologram(Location location, String name) {
    super(location, name);
  }

  @Override
  protected HologramLine createLine(Location location) {
    return new PlibHologramLine(location);
  }
}
