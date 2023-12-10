package com.gestankbratwurst.epro.teleports;

import com.gestankbratwurst.epro.EproCore;
import com.gestankbratwurst.epro.EproMinecraftGame;
import com.gestankbratwurst.epro.teleports.command.SpawnCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class SpawnLocationManager {

  private Location spawnLocation;

  public SpawnLocationManager() {
    EproMinecraftGame.register(new SpawnListener());
    EproCore.getPaperCommandManager().registerCommand(new SpawnCommand());
  }

  public Location getSpawnLocation() {
    return spawnLocation == null ? Bukkit.getWorlds().get(0).getSpawnLocation() : spawnLocation;
  }

  public void setSpawnLocation(Location spawnLocation) {
    this.spawnLocation = spawnLocation;
  }


  public void saveSpawn() {
    File file = new File(JavaPlugin.getPlugin(EproMinecraftGame.class).getDataFolder(), "spawn.yml");
    try {
      if (!file.exists()) {
        file.createNewFile();
      }
      FileConfiguration configuration = new YamlConfiguration();
      configuration.set("Spawn", spawnLocation);
      configuration.save(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void loadSpawn() {
    File file = new File(JavaPlugin.getPlugin(EproMinecraftGame.class).getDataFolder(), "spawn.yml");
    if (file.exists()) {
      FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
      spawnLocation = configuration.getLocation("Spawn");
    }
  }


}
