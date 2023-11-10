package com.gestankbratwurst.epro.holograms.abstraction;

import com.gestankbratwurst.epro.utils.spigot.UtilChunk;
import com.google.common.base.Preconditions;
import jdk.jshell.execution.Util;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHologram implements Hologram {

  private static final double LINE_SPACE = 0.275;

  private final List<HologramLine> hologramLines = new ArrayList<>();
  private final String name;
  private Location location;

  public AbstractHologram(Location location, String name) {
    this.name = name;
    this.location = location;
  }

  private Location getRelativeLocationForIndex(int index) {
    return location.clone().add(0, -LINE_SPACE * index, 0);
  }

  @Override
  public String getId() {
    return name;
  }

  @Override
  public int size() {
    return hologramLines.size();
  }

  @Override
  public void addLine(String line) {
    int nextIndex = size();
    Location lineLocation = getRelativeLocationForIndex(nextIndex);
    HologramLine hologramLine = createLine(lineLocation);
    hologramLine.setText(line);
    hologramLines.add(hologramLine);

    Chunk chunk = location.getChunk();
    Bukkit.getOnlinePlayers().forEach(player -> {
      if(UtilChunk.isChunkInView(player, chunk)) {
        hologramLine.showTo(player);
      }
    });
  }

  @Override
  public void setLine(int index, String line) {
    Preconditions.checkArgument(index < size());
    HologramLine hologramLine = hologramLines.get(index);
    hologramLine.setText(line);

    Chunk chunk = location.getChunk();
    Bukkit.getOnlinePlayers().forEach(player -> {
      if(UtilChunk.isChunkInView(player, chunk)) {
        hologramLine.updateTextFor(player);
      }
    });
  }

  @Override
  public String getLine(int index) {
    Preconditions.checkArgument(index < size());
    return hologramLines.get(index).getText();
  }

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public void teleport(Location target) {
    Chunk fromChunk = location.getChunk();
    Chunk toChunk = target.getChunk();
    this.location = target;
    for(int index = 0; index < size(); index++) {
      Location lineLoc = getRelativeLocationForIndex(index);
      HologramLine hologramLine = hologramLines.get(index);
      hologramLine.teleport(lineLoc);
      Bukkit.getOnlinePlayers().forEach(player -> {
        if(UtilChunk.isChunkInView(player, fromChunk) || UtilChunk.isChunkInView(player, toChunk)) {
          hologramLine.updateLocationFor(player);
        }
      });
    }
  }

  @Override
  public void showTo(Player player) {
    hologramLines.forEach(line -> line.showTo(player));
  }

  @Override
  public void hideFrom(Player player) {
    hologramLines.forEach(line -> line.hideFrom(player));
  }

  @Override
  public void removeLine(int index) {
    Preconditions.checkArgument(index < size());
    HologramLine line = hologramLines.remove(index);
    Chunk chunk = location.getChunk();

    Bukkit.getOnlinePlayers().forEach(player -> {
      if(UtilChunk.isChunkInView(player, chunk)) {
        line.hideFrom(player);
      }
    });

    teleport(this.location);
  }

  protected abstract HologramLine createLine(Location location);
}
