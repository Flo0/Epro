package com.gestankbratwurst.epro.teleports.impl;

import com.gestankbratwurst.epro.EproCore;
import com.gestankbratwurst.epro.actionbar.ActionBarBoard;
import com.gestankbratwurst.epro.actionbar.ActionBarSection;
import com.gestankbratwurst.epro.actionbar.ActionLine;
import com.gestankbratwurst.epro.tasks.TaskManager;
import com.gestankbratwurst.epro.teleports.PendingTeleport;
import com.gestankbratwurst.epro.utils.spigot.UtilGeometry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class StandardPlayerTeleport extends PendingTeleport {

  private static final double CIRCLE_RADIUS = 1.25;
  private static final double CIRCLE_HEIGHT_OFFSET = 2.0;
  private static final String TELEPORT_LAYER_ID = "TELEPORT_LAYER";
  private static final int RING_PARTICLE_COUNT = 3;
  private static final int ACTION_PRIORITY = ActionLine.HIGH_PRIORITY;

  public StandardPlayerTeleport(Player target, Location targetLocation, int delay) {
    super(target.getUniqueId(), targetLocation, delay);
  }

  @Override
  protected void onWaitTick() {
    Player player = Bukkit.getPlayer(this.targetId);
    if (player == null) {
      return;
    }
    Location base = player.getLocation().add(0, CIRCLE_HEIGHT_OFFSET, 0);
    Location particleLoc = UtilGeometry.getRandomLocationOnCircleXY(base, CIRCLE_RADIUS);
    World world = player.getWorld();
    world.spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, 1, 0, 0, 0, 0);

    Location targetBase = this.getTargetLocation().clone().add(0, CIRCLE_HEIGHT_OFFSET, 0);
    Location targetParticleLoc = UtilGeometry.getRandomLocationOnCircleXY(targetBase, CIRCLE_RADIUS);
    World targetWorld = this.getTargetLocation().getWorld();
    targetWorld.spawnParticle(Particle.ENCHANTMENT_TABLE, targetParticleLoc, 1, 0, 0, 0, 0);

    for (int i = 0; i < RING_PARTICLE_COUNT; i++) {
      Location randomRing = UtilGeometry.getRandomLocationOnCircleXY(base, CIRCLE_RADIUS);
      Location randomTargetRing = UtilGeometry.getRandomLocationOnCircleXY(targetBase, CIRCLE_RADIUS);
      world.spawnParticle(Particle.ELECTRIC_SPARK, randomRing, 1, 0, 0, 0, 0);
      targetWorld.spawnParticle(Particle.ELECTRIC_SPARK, randomTargetRing, 1, 0, 0, 0, 0);
    }
  }

  @Override
  protected void onTeleport() {
    Player player = Bukkit.getPlayer(this.targetId);
    if (player == null) {
      return;
    }

    Location from = player.getLocation();
    Location to = this.getTargetLocation();

    from.getWorld().playSound(from, Sound.ENTITY_SHULKER_TELEPORT, 1F, 1.5F);
    from.getWorld().playSound(from, Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 2.0F);
    TaskManager.runTaskLater(() -> to.getWorld().playSound(from, Sound.ENTITY_SHULKER_TELEPORT, 1F, 1.5F), 5);
    TaskManager.runTaskLater(() -> to.getWorld().playSound(from, Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 2.0F), 5);

    TaskManager.runTask(() -> removeActionLayer(player));
  }

  @Override
  protected void onStart() {
    Player player = Bukkit.getPlayer(this.targetId);
    if (player == null) {
      return;
    }
    addActionLayer(player);
  }

  @Override
  protected void onCancel() {
    Player player = Bukkit.getPlayer(this.targetId);
    if (player == null) {
      return;
    }
    removeActionLayer(player);
  }

  private void addActionLayer(Player player) {
    ActionBarBoard board = EproCore.getActionBarManager().getBoard(player);
    ActionBarSection section = board.getSection(ActionBarBoard.Section.MIDDLE);
    section.addLayer(TELEPORT_LAYER_ID, ACTION_PRIORITY, this::getPendingTeleportTimeDisplay);
  }

  private String getPendingTeleportTimeDisplay() {
    int ticksRemaining = this.getDelay() - this.getTicksWaiting();
    double seconds = ticksRemaining / 20D;
    return "§fTeleporting in §e%.1fs".formatted(seconds);
  }

  private void removeActionLayer(Player player) {
    ActionBarBoard board = EproCore.getActionBarManager().getBoard(player);
    ActionBarSection section = board.getSection(ActionBarBoard.Section.MIDDLE);
    section.remove(TELEPORT_LAYER_ID, ACTION_PRIORITY);
  }

}
