package com.gestankbratwurst.epro.holograms;

import com.gestankbratwurst.epro.holograms.abstraction.Hologram;
import com.gestankbratwurst.epro.holograms.abstraction.HologramFactory;
import com.gestankbratwurst.epro.holograms.implementations.AutoUpdatedHologram;
import com.gestankbratwurst.epro.tasks.TaskManager;
import com.gestankbratwurst.epro.utils.spigot.UtilChunk;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.*;
import java.util.function.Consumer;

public class HologramManager {

  private final Map<String, Hologram> hologramMap = new HashMap<>();
  private final Map<UUID, Map<Long, List<Hologram>>> hologramWorldMap = new HashMap<>();
  private final Set<AutoUpdatedHologram> autoUpdatedHolograms = new HashSet<>();
  private final HologramFactory hologramFactory;


  public HologramManager(HologramFactory hologramFactory) {
    this.hologramFactory = hologramFactory;
    TaskManager.runTaskTimer(this::tickUpdatableHolograms, 1L, 1L);
  }

  public Hologram getHologram(String hologramID) {
    return hologramMap.get(hologramID);
  }

  public Hologram createHologram(Location location, String name) {
    if (hologramMap.containsKey(name)) {
      return null;
    }

    Hologram hologram = hologramFactory.createHologram(location, name);
    registerHologram(location, name, hologram);

    return hologram;
  }

  public AutoUpdatedHologram createUpdatedHologram(Location location, String name, Consumer<Hologram> updateConsumer, int updateTicks) {
    if (hologramMap.containsKey(name)) {
      return null;
    }
    Hologram hologram = hologramFactory.createHologram(location, name);
    AutoUpdatedHologram autoUpdatedHologram = new AutoUpdatedHologram(hologram, updateConsumer, updateTicks);
    registerHologram(location, name, autoUpdatedHologram);
    autoUpdatedHolograms.add(autoUpdatedHologram);

    return autoUpdatedHologram;
  }

  private void registerHologram(Location location, String name, Hologram autoUpdatedHologram) {
    hologramMap.put(name, autoUpdatedHologram);

    UUID worldId = location.getWorld().getUID();
    long chunkKey = UtilChunk.getChunkKey(location);

    hologramWorldMap.computeIfAbsent(worldId, key -> new HashMap<>())
            .computeIfAbsent(chunkKey, key -> new ArrayList<>())
            .add(autoUpdatedHologram);
  }

  public void deleteHologram(String hologramID) {
    Hologram hologram = hologramMap.remove(hologramID);
    if (hologram == null) {
      return;
    }
    
    if (hologram instanceof AutoUpdatedHologram) {
      autoUpdatedHolograms.remove(hologram);
    }

    Bukkit.getOnlinePlayers().forEach(hologram::hideFrom);

    Location location = hologram.getLocation();
    UUID worldId = location.getWorld().getUID();
    long chunkKey = UtilChunk.getChunkKey(location);

    Map<Long, List<Hologram>> chunkMap = hologramWorldMap.get(worldId);

    if (chunkMap == null) {
      return;
    }

    List<Hologram> holograms = chunkMap.get(chunkKey);

    if (holograms == null) {
      return;
    }

    holograms.remove(hologram);

    if (holograms.isEmpty()) {
      chunkMap.remove(chunkKey);
    }
  }

  public void displayHologramsInChunk(Chunk chunk, Player player) {
    forEachHologramInChunk(chunk, hologram -> hologram.showTo(player));
  }

  public void hideHologramsInChunk(Chunk chunk, Player player) {
    forEachHologramInChunk(chunk, hologram -> hologram.hideFrom(player));
  }

  private void forEachHologramInChunk(Chunk chunk, Consumer<Hologram> consumer) {
    Map<Long, List<Hologram>> chunkMap = this.hologramWorldMap.get(chunk.getWorld().getUID());
    if (chunkMap == null) {
      return;
    }
    List<Hologram> hologramList = chunkMap.get(chunk.getChunkKey());
    if (hologramList == null) {
      return;
    }
    hologramList.forEach(consumer);
  }

  public List<String> getHologramNames() {
    return List.copyOf(hologramMap.keySet());
  }

  private void tickUpdatableHolograms() {
    autoUpdatedHolograms.forEach(AutoUpdatedHologram::run);
  }

}
