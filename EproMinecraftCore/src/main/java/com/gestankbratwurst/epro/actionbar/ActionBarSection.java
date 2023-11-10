package com.gestankbratwurst.epro.actionbar;

import com.gestankbratwurst.epro.tasks.TaskManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;


public class ActionBarSection {

  protected ActionBarSection() {
    this.actionLineSet = new TreeSet<>();
    this.tempTasks = new HashMap<>();
    addLayer(ActionLine.defaultLine());
  }

  private final SortedSet<ActionLine> actionLineSet;
  private final Map<ActionLine, BukkitTask> tempTasks;

  public void addLayer(String layerId, int priority, Supplier<String> lineSupplier) {
    addLayer(new ActionLine(layerId, priority, lineSupplier));
  }

  public void addLayer(ActionLine line) {
    this.actionLineSet.add(line);
  }

  public void removeLayer(ActionLine line) {
    this.actionLineSet.remove(line);
    tempTasks.remove(line);
  }

  public void remove(String key) {
    removeLayer(new ActionLine(key, 0, (Supplier<String>) null));
  }

  public void addTempLayer(long lifeTicks, ActionLine line) {
    addLayer(line);
    Optional.ofNullable(tempTasks.remove(line)).ifPresent(BukkitTask::cancel);
    tempTasks.put(line, TaskManager.runTaskLater(() -> this.removeLayer(line), lifeTicks));
  }

  public ActionLine getMostSignificantLayer() {
    return this.actionLineSet.first();
  }

}
