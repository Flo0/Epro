package com.gestankbratwurst.epro.playerdata;

import com.gestankbratwurst.epro.moderation.UserStatus;
import com.gestankbratwurst.epro.moderation.UserStatusRequest;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class EproPlayerListener implements Listener {

  private final EproPlayerManager eproPlayerManager;

  public EproPlayerListener(EproPlayerManager eproPlayerManager) {
    this.eproPlayerManager = eproPlayerManager;
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPreJoin(AsyncPlayerPreLoginEvent event) {
    if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
      return;
    }
    eproPlayerManager.loadPlayerData(event.getUniqueId());
  }



}
