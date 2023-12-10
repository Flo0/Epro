package com.gestankbratwurst.epro;

import com.gestankbratwurst.epro.gson.GsonSerializer;
import com.gestankbratwurst.epro.mongodb.MongoDriverProperties;
import com.gestankbratwurst.epro.resourcepack.assembly.BlockModel;
import com.gestankbratwurst.epro.resourcepack.assembly.CustomSound;
import com.gestankbratwurst.epro.resourcepack.assembly.TextureModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class EproConfiguration implements EproCoreConfigurationService {

  @SneakyThrows
  public static EproConfiguration load() {
    File folder = JavaPlugin.getPlugin(EproMinecraftGame.class).getDataFolder();
    if (!folder.exists()) {
      folder.mkdirs();
    }
    File configFile = new File(folder, "config.json");
    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    EproConfiguration configuration = new EproConfiguration();
    if (!configFile.exists()) {
      String json = gson.toJson(configuration);
      Files.writeString(configFile.toPath(), json);
    }
    String json = Files.readString(configFile.toPath());
    return gson.fromJson(json, EproConfiguration.class);
  }

  private final transient EproGameSerializer serializer;
  private final String redisAddress = "redis://localhost:6379";
  private final MongoDriverProperties mongoDriverProperties = MongoDriverProperties.builder()
      .hostAddress("localhost")
      .hostPort(27017)
      .user("admin")
      .password("admin")
      .build();

  public EproConfiguration() {
    this.serializer = new EproGameSerializer();
  }

  @Override
  public GsonSerializer getSerializer() {
    return this.serializer;
  }

  @Override
  public String getRedisAddress() {
    return this.redisAddress;
  }

  @Override
  public MongoDriverProperties getMongoDriverProperties() {
    return this.mongoDriverProperties;
  }

  @Override
  public boolean isResourcepackEnabled() {
    return false;
  }

  @Override
  public List<CustomSound> getCustomSounds() {
    return List.of();
  }

  @Override
  public List<TextureModel> getTextureModels() {
    return List.of();
  }

  @Override
  public List<BlockModel> getBlockModels() {
    return List.of();
  }



}
