package com.gestankbratwurst.epro.teleports;

import com.gestankbratwurst.epro.EproMinecraftGame;
import com.gestankbratwurst.epro.messaging.Msg;
import com.gestankbratwurst.epro.tasks.TaskManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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

  @EventHandler
  public void onTeleport(PlayerTeleportEvent event) {
    if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
      event.setCancelled(true);
      TaskManager.runTask(() -> {
        event.setTo(EproMinecraftGame.getSpawnLocationManager().getSpawnLocation());
        Msg.sendWarning(event.getPlayer(), "Du kannst nicht durch das Endportal teleportieren.");
      });
    }
  }

}
