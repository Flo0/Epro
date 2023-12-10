package com.gestankbratwurst.epro;

import com.gestankbratwurst.epro.gson.GsonSerializer;
import com.gestankbratwurst.epro.gson.LocationSerializer;
import org.bukkit.Location;

public class EproGameSerializer extends GsonSerializer {

  public EproGameSerializer() {
    super();
    this.registerTypeAdapter(Location.class, new LocationSerializer());
  }

}
