package com.gestankbratwurst.epro;

import com.gestankbratwurst.epro.statistics.StatisticManager;
import com.gestankbratwurst.epro.tasks.TaskManager;
import com.gestankbratwurst.epro.teleports.SpawnLocationManager;
import com.gestankbratwurst.epro.teleports.TeleportManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public final class EproMinecraftGame extends EproPlugin {

  private static final int TICKS_PER_FLUSH = 15 * 60 * 20;

  private static EproMinecraftGame instance;
  @Getter
  private static TeleportManager teleportManager;
  @Getter
  private static SpawnLocationManager spawnLocationManager;
  @Getter
  private static StatisticManager statisticManager;

  public static void register(Listener listener) {
    Bukkit.getPluginManager().registerEvents(listener, instance);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    instance = this;
  }

  @Override
  public void onEnable() {
    super.onEnable();
    teleportManager = new TeleportManager();
    spawnLocationManager = new SpawnLocationManager();
    statisticManager = new StatisticManager();
    TaskManager.runTaskTimerAsync(() -> TaskManager.runOnIOPool(this::flushData), TICKS_PER_FLUSH, TICKS_PER_FLUSH);
  }

  @Override
  public void onDisable() {
    super.onDisable();
  }

  @Override
  public EproCoreConfigurationService provideConfig() {
    getLogger().info("Loading EproConfiguration...");
    return EproConfiguration.load();
  }

  private void flushData() {
    getLogger().info("Flushing data to database...");
    spawnLocationManager.flush();
  }

}
