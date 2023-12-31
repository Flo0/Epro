package com.gestankbratwurst.epro.teleports.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.gestankbratwurst.epro.EproMinecraftGame;
import com.gestankbratwurst.epro.messaging.Msg;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("spawn")
public class SpawnCommand extends BaseCommand {

  @Default
  public void onSpawn(Player player) {
    Msg.sendInfo(player, "Du wirst gleich zum {} teleportiert.", "Spawn");
    Location spawnLocation = EproMinecraftGame.getSpawnLocationManager().getSpawnLocation();
    EproMinecraftGame.getTeleportManager().teleportPlayer(player, spawnLocation, 100);
  }

  @Subcommand("set")
  @CommandPermission("admin")
  public void onSetSpawn(Player player) {
    Location location = player.getLocation();
    EproMinecraftGame.getSpawnLocationManager().setSpawnLocation(location);
    Msg.sendInfo(player, "Der Spawn wurde gesetzt.");
  }

}
