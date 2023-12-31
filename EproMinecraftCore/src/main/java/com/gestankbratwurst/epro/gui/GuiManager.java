package com.gestankbratwurst.epro.gui;

import com.gestankbratwurst.epro.gui.abstraction.AutoUpdated;
import com.gestankbratwurst.epro.gui.abstraction.InventoryHandler;
import com.gestankbratwurst.epro.gui.baseimpl.GuiHandler;
import com.gestankbratwurst.epro.tasks.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {

  private final Map<Inventory, InventoryHandler> handlerMap = new HashMap<>();
  private long updateTick = 0;

  public GuiManager(JavaPlugin plugin) {
    Bukkit.getPluginManager().registerEvents(new GuiListener(this), plugin);
    TaskManager.runTaskTimer(this::checkForUpdatableOpenInventories, 1, 1);
  }

  private void checkForUpdatableOpenInventories() {
    updateTick++;
    handlerMap.values().stream()
            .filter(AutoUpdated.class::isInstance)
            .map(AutoUpdated.class::cast)
            .filter(autoUpdated -> updateTick % autoUpdated.getUpdateInterval() == 0)
            .forEach(AutoUpdated::update);
  }

  public void initializeAndRegister(GuiHandler guiHandler) {
    Inventory inventory = guiHandler.initializeInventory();
    guiHandler.decorate();
    this.registerHandledInventory(inventory, guiHandler);
  }

  public void registerHandledInventory(Inventory inventory, InventoryHandler handler) {
    this.handlerMap.put(inventory, handler);
  }

  public void unregisterInventory(Inventory inventory) {
    this.handlerMap.remove(inventory);
  }

  public InventoryHandler getHandlerOf(Inventory inventory) {
    return this.handlerMap.get(inventory);
  }

  protected void handleClick(InventoryClickEvent event) {
    InventoryHandler handler = this.getHandlerOf(event.getInventory());
    if (handler != null) {
      handler.onClick(event);
    }
  }

  protected void handleOpen(InventoryOpenEvent event) {
    InventoryHandler handler = this.getHandlerOf(event.getInventory());
    if (handler != null) {
      handler.onOpen(event);
    }
  }

  protected void handleClose(InventoryCloseEvent event) {
    Inventory inventory = event.getInventory();
    InventoryHandler handler = this.getHandlerOf(inventory);
    if (handler != null) {
      handler.onClose(event);
      if (handler.unregisterOnClose()) {
        this.unregisterInventory(inventory);
      }
    }
  }

}
