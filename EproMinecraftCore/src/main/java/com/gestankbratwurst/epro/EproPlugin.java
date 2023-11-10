package com.gestankbratwurst.epro;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class EproPlugin extends JavaPlugin {

  @Override
  public void onLoad() {
    Bukkit.getServicesManager().register(EproCoreConfigurationService.class, provideConfig(), this, ServicePriority.Normal);
  }

  public abstract EproCoreConfigurationService provideConfig();

}
