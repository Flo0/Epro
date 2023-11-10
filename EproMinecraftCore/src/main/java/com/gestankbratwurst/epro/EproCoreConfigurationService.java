package com.gestankbratwurst.epro;

import com.gestankbratwurst.epro.gson.GsonSerializer;
import com.gestankbratwurst.epro.mongodb.MongoDriverProperties;
import com.gestankbratwurst.epro.resourcepack.assembly.BlockModel;
import com.gestankbratwurst.epro.resourcepack.assembly.CustomSound;
import com.gestankbratwurst.epro.resourcepack.assembly.TextureModel;

import java.io.File;
import java.util.Collections;
import java.util.List;

public interface EproCoreConfigurationService {

  GsonSerializer getSerializer();

  String getRedisAddress();

  MongoDriverProperties getMongoDriverProperties();

  default String resourcepackServerHost() {
    return "127.0.0.1";
  }

  default int resourcepackServerPort() {
    return 7786;
  }

  default boolean isResourcepackEnabled() {
    return false;
  }

  default File getRawResourcepackFiles() {
    return null;
  }

  default List<CustomSound> getCustomSounds() {
    return Collections.emptyList();
  }

  default List<TextureModel> getTextureModels() {
    return Collections.emptyList();
  }

  default List<BlockModel> getBlockModels() {
    return Collections.emptyList();
  }

  default String getMessagePrefix() {
    return "Epro";
  }

  default int getRedisRetryInterval() {
    return 2000;
  }

  default int getRedisTimeout() {
    return 5000;
  }

  default int getRedisConnectionPoolSize() {
    return 64;
  }

  default int getRedisNettyThreads() {
    return 32;
  }

  default boolean isRedisEnabled() {
    return false;
  }
}