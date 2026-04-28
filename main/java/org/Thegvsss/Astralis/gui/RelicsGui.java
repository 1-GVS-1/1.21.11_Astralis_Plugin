package org.Thegvsss.Astralis.utils.gui;

import org.Thegvsss.Astralis.items.relics.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class RelicsGui implements InventoryHolder {

    public static final String TITLE = "§6Relics";
    private final Inventory inventory;

    public RelicsGui() {
        inventory = Bukkit.createInventory(this, 9, TITLE);

        inventory.setItem(0, Dharma.create());
        inventory.setItem(1, GravityFeather.create());
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void handle(Player player, int slot) {
        if (slot == 0) player.getInventory().addItem(Dharma.create());
        if (slot == 1) player.getInventory().addItem(GravityFeather.create());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}