package com.gestankbratwurst.epro.utils.spigot;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class NamespaceFactory {

  public static void init(final JavaPlugin plugin) {
    if (instance == null) {
      instance = new NamespaceFactory(plugin);
    }
  }

  private static NamespaceFactory instance;

  private NamespaceFactory(final JavaPlugin plugin) {
    this.cachedKeys = new Object2ObjectOpenHashMap<>();
    this.plugin = plugin;
  }

  private final JavaPlugin plugin;
  private final Map<String, NamespacedKey> cachedKeys;

  public static NamespacedKey provide(final String key) {
    NamespacedKey nsk = instance.cachedKeys.get(key);
    if (nsk == null) {
      nsk = new NamespacedKey(instance.plugin, key);
      instance.cachedKeys.put(key, nsk);
    }
    return nsk;
  }

}
