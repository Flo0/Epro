package com.gestankbratwurst.epro.gui.util;

import com.gestankbratwurst.epro.gui.abstraction.GuiButton;
import com.gestankbratwurst.epro.gui.baseimpl.DynamicGUI;
import com.gestankbratwurst.epro.tasks.TaskManager;
import com.gestankbratwurst.epro.utils.spigot.ItemBuilder;
import com.gestankbratwurst.epro.utils.spigot.UtilPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.Consumer;

public class ConfirmationGUI extends DynamicGUI {

  private final Consumer<Boolean> consumer;

  public ConfirmationGUI(Consumer<Boolean> consumer) {
    this.consumer = consumer;
  }

  @Override
  protected void setupButtons() {
    this.setButton(2, this.createAcceptButton());
    this.setButton(6, this.createDenyButton());
  }

  private GuiButton createAcceptButton() {
    return GuiButton.builder()
            .eventConsumer(event -> {
              TaskManager.runTask(() -> this.consumer.accept(true));
              UtilPlayer.playUIClick((Player) event.getWhoClicked());
            }).iconCreator(() -> new ItemBuilder(Material.GREEN_WOOL)
                    .name("§aYes")
                    .build())
            .build();
  }

  private GuiButton createDenyButton() {
    return GuiButton.builder()
            .eventConsumer(event -> {
              TaskManager.runTask(() -> this.consumer.accept(false));
              UtilPlayer.playUIClick((Player) event.getWhoClicked());
            }).iconCreator(() -> new ItemBuilder(Material.RED_WOOL)
                    .name("§cNo")
                    .build())
            .build();
  }

  @Override
  protected Inventory createInventory() {
    return Bukkit.createInventory(null, 3 * 9, Component.text("§cAre you sure?"));
  }

  @Override
  public void onClose(InventoryCloseEvent event) {
    TaskManager.runTask(() -> this.consumer.accept(false));
    super.onClose(event);
  }
}
