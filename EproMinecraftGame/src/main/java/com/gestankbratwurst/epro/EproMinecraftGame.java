package com.gestankbratwurst.epro;

public final class EproMinecraftGame extends EproPlugin {

  @Override
  public void onEnable() {
    // Plugin startup logic

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
