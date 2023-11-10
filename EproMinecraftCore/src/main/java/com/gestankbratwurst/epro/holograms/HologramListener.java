package com.gestankbratwurst.epro.holograms;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HologramListener implements Listener {

  private final HologramManager hologramManager;

  public HologramListener(HologramManager hologramManager) {
    this.hologramManager = hologramManager;
  }

  @EventHandler
  public void onDisplay(PlayerChunkLoadEvent event) {
    hologramManager.displayHologramsInChunk(event.getChunk(), event.getPlayer());
  }

  @EventHandler
  public void onDrop(PlayerChunkUnloadEvent event) {
    hologramManager.hideHologramsInChunk(event.getChunk(), event.getPlayer());
  }

}
