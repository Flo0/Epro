package com.gestankbratwurst.epro.teleports;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TeleportListener implements Listener {

  private final TeleportManager teleportManager;

  public TeleportListener(TeleportManager teleportManager) {
    this.teleportManager = teleportManager;
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    if (event.hasChangedBlock()) {
      this.teleportManager.interruptTeleport(event.getPlayer().getUniqueId());
    }
  }

  @EventHandler
  public void onDamage(EntityDamageEvent event) {
    this.teleportManager.interruptTeleport(event.getEntity().getUniqueId());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    this.teleportManager.interruptTeleport(event.getPlayer().getUniqueId());
  }

}
