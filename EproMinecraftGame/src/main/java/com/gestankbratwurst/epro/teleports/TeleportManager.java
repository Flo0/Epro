package com.gestankbratwurst.epro.teleports;

import com.gestankbratwurst.epro.EproCore;
import com.gestankbratwurst.epro.tasks.TaskManager;
import com.gestankbratwurst.epro.teleports.command.TeleportCommand;
import com.gestankbratwurst.epro.teleports.impl.StandardPlayerTeleport;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TeleportManager {

  private final Map<UUID, PendingTeleport> pendingTeleports = new HashMap<>();

  public TeleportManager() {
    TaskManager.runTaskTimer(this::tickPendingTeleports, 1, 1);
    EproCore.getPaperCommandManager().registerCommand(new TeleportCommand());
  }

  public void teleport(PendingTeleport teleport) {
    pendingTeleports.put(teleport.targetId, teleport);
  }

  public void teleportPlayer(Player player, Location location, int delay) {
    teleport(new StandardPlayerTeleport(player, location, delay));
  }

  public void interruptTeleport(UUID targetId) {
    this.removeTeleport(targetId);
  }

  protected void tickPendingTeleports() {
    List.copyOf(pendingTeleports.values()).forEach(this::tickTeleport);
  }

  private void addTeleport(PendingTeleport pendingTeleport) {
    Optional.ofNullable(pendingTeleports.put(pendingTeleport.getTargetId(), pendingTeleport))
        .ifPresent(PendingTeleport::onCancel);
  }

  private void removeTeleport(UUID targetId) {
    Optional.ofNullable(pendingTeleports.remove(targetId))
        .ifPresent(PendingTeleport::onCancel);
  }

  private void tickTeleport(PendingTeleport pendingTeleport) {
    pendingTeleport.run();
    if (pendingTeleport.isDone()) {
      pendingTeleports.remove(pendingTeleport.getTargetId());
    }
  }

}
