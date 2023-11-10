package com.gestankbratwurst.epro.tablist.implementation;

import java.util.UUID;

public abstract class ScheduledSingleUserTabList extends SingleUserTabList {

  private final int scheduledInterval;
  private long counter = 0L;

  public ScheduledSingleUserTabList(UUID userID, int scheduledInterval) {
    super(userID);
    this.scheduledInterval = scheduledInterval;
  }

  public abstract void tickAction();

  public final void onTick() {
    if (++counter % scheduledInterval == 0) {
      tickAction();
    }
  }

}
