package com.gestankbratwurst.epro.holograms.implementations.nms;

import com.gestankbratwurst.epro.holograms.abstraction.AbstractHologram;
import com.gestankbratwurst.epro.holograms.abstraction.HologramLine;
import org.bukkit.Location;

public class NMSHologram extends AbstractHologram {

  public NMSHologram(Location location, String name) {
    super(location, name);
  }

  @Override
  protected HologramLine createLine(Location location) {
    return new NMSHologramLine(location);
  }

}
