package com.gestankbratwurst.epro;

import com.gestankbratwurst.epro.teleports.TeleportManager;
import lombok.Getter;

public final class EproMinecraftGame extends EproPlugin {

  @Getter
  private static TeleportManager teleportManager;

  @Override
  public void onEnable() {
    teleportManager = new TeleportManager();

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  @Override
  public EproCoreConfigurationService provideConfig() {
    getLogger().info("Loading EproConfiguration...");
    return EproConfiguration.load();
  }
}
