package com.gestankbratwurst.epro.teleports;

import com.gestankbratwurst.epro.EproCore;
import com.gestankbratwurst.epro.EproMinecraftGame;
import com.gestankbratwurst.epro.mongodb.MongoBackedMap;
import com.gestankbratwurst.epro.teleports.command.SpawnCommand;
import com.mongodb.client.MongoCollection;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.Flushable;

public class SpawnLocationManager implements Flushable {

  private static final String SPAWN_LOCATION_KEY = "spawn-location";

  private final MongoBackedMap<String, Location> spawnLocationStore;

  public SpawnLocationManager() {
    EproMinecraftGame.register(new SpawnListener());
    EproCore.getPaperCommandManager().registerCommand(new SpawnCommand());
    MongoCollection<Location> spawnLocationCollection = EproCore.getDatabase().getCollection(SPAWN_LOCATION_KEY, Location.class);
    this.spawnLocationStore = new MongoBackedMap<>(spawnLocationCollection, EproCore.getSerializer(), String.class);
    this.spawnLocationStore.syncLocalToRemote();
  }

  public Location getSpawnLocation() {
    Location location = spawnLocationStore.get(SPAWN_LOCATION_KEY);
    if (location == null) {
      return Bukkit.getWorlds().get(0).getSpawnLocation();
    }
    return location;
  }

  public void setSpawnLocation(Location location) {
    spawnLocationStore.put(SPAWN_LOCATION_KEY, location);
  }

  @Override
  public void flush() {
    spawnLocationStore.syncRemoteToLocal();
  }
}
