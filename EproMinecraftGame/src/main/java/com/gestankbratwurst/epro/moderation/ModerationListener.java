package com.gestankbratwurst.epro.moderation;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class ModerationListener implements Listener {

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPreJoinBlock(AsyncPlayerPreLoginEvent event) {
    UserStatus status = UserStatusRequest.get(event.getUniqueId());
    switch (status) {
      case NOT_REGISTERED -> event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("§eDu bist nicht registriert."));
      case BANNED -> event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Component.text("§cDu wurdest gebannt."));
      case ERROR -> event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("§cEs ist ein Fehler aufgetreten."));
      default -> {
      }
    }
  }

}
