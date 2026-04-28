package org.thegvsss.astralis.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Object holder = event.getView().getTopInventory().getHolder();
        if (holder == null) return;
        if (event.getClickedInventory() == null) return;

        // Não cancela o inventário do próprio player
        if (event.getClickedInventory().equals(player.getInventory())) return;

        event.setCancelled(true);

        int slot = event.getSlot();

        if (holder instanceof AstralisGui gui) gui.handle(player, slot);
        else if (holder instanceof WeaponsGui gui) gui.handle(player, slot);
        else if (holder instanceof RelicsGui gui) gui.handle(player, slot);
    }
}
