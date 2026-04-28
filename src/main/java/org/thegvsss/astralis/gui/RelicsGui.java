package org.thegvsss.astralis.gui;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.thegvsss.astralis.items.relics.*;

public class RelicsGui implements InventoryHolder {

    public static final String TITLE = "§6Relics";
    private final Inventory inventory;

    public RelicsGui() {
        inventory = Bukkit.createInventory(this, 9, TITLE);
        inventory.setItem(0, Dharma.create());
        inventory.setItem(1, GravityFeather.create());
        inventory.setItem(2, ForbidenTotem.create());
        inventory.setItem(3, PhoenixFeather.create());
        inventory.setItem(4, TimeShard.create());
    }

    public void open(Player player) { player.openInventory(inventory); }

    public void handle(Player player, int slot) {
        switch (slot) {
            case 0 -> player.getInventory().addItem(Dharma.create());
            case 1 -> player.getInventory().addItem(GravityFeather.create());
            case 2 -> player.getInventory().addItem(ForbidenTotem.create());
            case 3 -> player.getInventory().addItem(PhoenixFeather.create());
            case 4 -> player.getInventory().addItem(TimeShard.create());
        }
    }

    @Override
    public Inventory getInventory() { return inventory; }
}
