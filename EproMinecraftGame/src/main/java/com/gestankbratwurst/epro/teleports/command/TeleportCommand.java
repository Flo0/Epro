package com.gestankbratwurst.epro.teleports.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.gestankbratwurst.epro.EproMinecraftGame;
import com.gestankbratwurst.epro.messaging.Msg;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@CommandAlias("teleport|tp")
@CommandPermission("epro.teleport")
public class TeleportCommand extends BaseCommand {

  @Default
  public void onCommand(Player player) {
    player.sendMessage("§e/teleport here <player>");
    player.sendMessage("§e/teleport to <player>");
  }

  @Subcommand("here")
  public void onTpHere(Player player, OnlinePlayer target) {
    target.getPlayer().teleport(player.getLocation());
    Msg.sendInfo(player, "You have teleported {} to you.", target.getPlayer().getName());
  }

  @Subcommand("to")
  public void onTpTo(Player player, OnlinePlayer target) {
    player.teleport(target.getPlayer().getLocation());
    Msg.sendInfo(player, "You have teleported to {}", target.getPlayer().getName());
  }

  @Subcommand("right")
  public void onTpRight(Player player) {
    Location base = player.getLocation();
    Vector forward = player.getFacing().getDirection();
    Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize();
    Location target = base.clone().add(right.clone().multiply(7));
    EproMinecraftGame.getTeleportManager().teleportPlayer(player, target, 100);
  }

}
