package com.gestankbratwurst.epro.epro;

import com.gestankbratwurst.epro.gson.GsonSerializer;
import com.gestankbratwurst.epro.gson.adapters.UUIDAdapter;

import java.util.UUID;

public class EproNetworkGsonSerializer extends GsonSerializer {

  public EproNetworkGsonSerializer() {
    this.registerTypeAdapter(UUID.class, new UUIDAdapter());
  }

}
