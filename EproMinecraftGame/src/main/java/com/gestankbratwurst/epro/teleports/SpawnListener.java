package com.gestankbratwurst.epro.teleports;

import com.gestankbratwurst.epro.EproMinecraftGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnListener implements Listener {

  @EventHandler
  public void onRespawn(PlayerRespawnEvent event) {
    if (event.isBedSpawn() || event.isAnchorSpawn()) {
      return;
    }
    event.setRespawnLocation(EproMinecraftGame.getSpawnLocationManager().getSpawnLocation());
  }

  @EventHandler
  public void onFirstSpawn(PlayerSpawnLocationEvent event) {
    if (event.getPlayer().hasPlayedBefore()) {
      return;
    }
    event.setSpawnLocation(EproMinecraftGame.getSpawnLocationManager().getSpawnLocation());
  }

}
