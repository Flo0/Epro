package com.gestankbratwurst.epro.blockdata;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BlockDataManager {

  private final Object2ObjectMap<UUID, BlockDataWorldDomain> worldMap = new Object2ObjectOpenHashMap<>();

  public BlockDataManager(JavaPlugin plugin) {
    init();
    Bukkit.getPluginManager().registerEvents(new BlockDataListener(this), plugin);
  }

  private void init() {
    Bukkit.getWorlds().forEach(this::initWorld);
  }

  public void terminate() {
    Bukkit.getWorlds().forEach(this::terminateWorld);
  }

  protected void initWorld(World world) {
    for (Chunk chunk : world.getLoadedChunks()) {
      initChunk(chunk);
    }
  }

  protected void terminateWorld(World world) {
    for (Chunk chunk : world.getLoadedChunks()) {
      terminateChunk(chunk);
    }
  }

  protected void initChunk(Chunk chunk) {
    worldMap.computeIfAbsent(chunk.getWorld().getUID(), BlockDataWorldDomain::new).initChunk(chunk);
  }

  protected void terminateChunk(Chunk chunk) {
    worldMap.computeIfAbsent(chunk.getWorld().getUID(), BlockDataWorldDomain::new).terminateChunk(chunk);
  }

  protected void terminateBlock(Block block) {
    clearDataOf(block);
  }

  public Optional<PersistentDataContainer> getDataOf(Block block) {
    return worldMap.computeIfAbsent(block.getWorld().getUID(), BlockDataWorldDomain::new).getDataOf(block);
  }

  public boolean hasData(Block block) {
    return getDataOf(block).isPresent();
  }

  public PersistentDataContainer getOrCreateData(Block block) {
    return worldMap.computeIfAbsent(block.getWorld().getUID(), BlockDataWorldDomain::new).createData(block);
  }

  protected void putData(Block block, PersistentDataContainer container) {
    worldMap.computeIfAbsent(block.getWorld().getUID(), BlockDataWorldDomain::new).putData(block, container);
  }

  public void clearDataOf(Block block) {
    worldMap.computeIfAbsent(block.getWorld().getUID(), BlockDataWorldDomain::new).clearDataOf(block);
  }

  protected void shiftDataInDirection(BlockFace direction, List<Block> blocks) {
    Map<Block, PersistentDataContainer> targetMap = new HashMap<>();
    for (Block block : blocks) {
      getDataOf(block).ifPresent(data -> {
        Block relative = block.getRelative(direction);
        targetMap.put(relative, data);
      });
    }
    blocks.forEach(this::clearDataOf);
    targetMap.forEach(this::putData);
  }

  public void moveDataFromTo(Block from, Block to) {
    getDataOf(from).ifPresent(fromData -> putData(to, fromData));
  }

  public Map<Block, PersistentDataContainer> getDataInChunk(Chunk chunk) {
    return worldMap.computeIfAbsent(chunk.getWorld().getUID(), BlockDataWorldDomain::new).getDataInChunk(chunk);
  }

}
