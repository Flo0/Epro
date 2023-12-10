package com.gestankbratwurst.epro.start;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.gestankbratwurst.epro.messaging.Msg;
import org.bukkit.entity.Player;

@CommandPermission("admin")
@CommandAlias("start")
public class StartCommand extends BaseCommand {

  private final StartManager startManager;

  public StartCommand() {
    startManager = new StartManager();
  }

  @Default
  public void onDefault(Player player) {
    Msg.sendInfo(player, "{} zum aufbauen.", "/start build");
    Msg.sendInfo(player, "{} zum starten.", "/start play");
  }

  @Subcommand("play")
  public void onPlay(Player player) {
    startManager.startGame();
    Msg.sendInfo(player, "Das Spiel wurde gestartet.");
  }

  @Subcommand("build")
  public void onReset(Player player) {
    startManager.reset();
    Msg.sendInfo(player, "Die Starttüren wurden zurückgesetzt.");
  }

}
