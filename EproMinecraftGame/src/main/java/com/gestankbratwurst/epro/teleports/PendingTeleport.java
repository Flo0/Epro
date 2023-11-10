package com.gestankbratwurst.epro.teleports;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

@Getter
public abstract class PendingTeleport implements Runnable {

  protected final UUID targetId;
  protected final Location targetLocation;
  protected final int delay;
  protected int ticksWaiting;

  protected PendingTeleport(UUID targetId, Location targetLocation, int delay) {
    this.targetId = targetId;
    this.targetLocation = targetLocation;
    this.delay = delay;
  }

  public boolean isDone() {
    return ticksWaiting >= delay;
  }

  @Override
  public void run() {
    if (ticksWaiting == 0) {
      onStart();
    }
    ticksWaiting++;
    if (isDone()) {
      this.onTeleport();
      Entity entity = Bukkit.getEntity(targetId);
      if (entity == null || !entity.isValid()) {
        return;
      }
      entity.teleport(targetLocation);
      return;
    }
    onWaitTick();
  }

  protected abstract void onWaitTick();

  protected abstract void onTeleport();

  protected abstract void onStart();

  protected abstract void onCancel();

}
