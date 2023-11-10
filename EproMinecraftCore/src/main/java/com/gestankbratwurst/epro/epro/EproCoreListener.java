package com.gestankbratwurst.epro.epro;

import com.gestankbratwurst.epro.EproGateway;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EproCoreListener implements Listener {

  @EventHandler
  public void onPreLogin(AsyncPlayerPreLoginEvent event) {
    EproGateway.getEproNetworkUserManager().applyToRemoteUser(event.getUniqueId(), user -> {
      user.setLastSeenMinecraftName(event.getName());
      user.setMinecraftUid(event.getUniqueId());
      user.setLastLoginTime(System.currentTimeMillis());
    });
    EproGateway.getEproNetworkUserManager().cache(event.getUniqueId());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    EproGateway.getEproNetworkUserManager().uncache(event.getPlayer().getUniqueId());
  }

}
