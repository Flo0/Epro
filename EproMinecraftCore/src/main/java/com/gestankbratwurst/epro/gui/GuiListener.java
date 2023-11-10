package com.gestankbratwurst.epro.gui;

import com.gestankbratwurst.epro.EproCore;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

@RequiredArgsConstructor
public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        EproCore.getGuiManager().handleClick(event);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        EproCore.getGuiManager().handleOpen(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        EproCore.getGuiManager().handleClose(event);
    }

}
