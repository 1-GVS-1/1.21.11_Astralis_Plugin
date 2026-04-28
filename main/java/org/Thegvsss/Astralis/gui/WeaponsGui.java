package org.Thegvsss.Astralis.utils.gui;

import org.Thegvsss.Astralis.items.weapons.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class WeaponsGui implements InventoryHolder {

    public static final String TITLE = "§cWeapons";
    private final Inventory inventory;

    public WeaponsGui() {
        inventory = Bukkit.createInventory(this, 9, TITLE);

        inventory.setItem(0, SwordOfCrushing.create());
        inventory.setItem(1, EnderSword.create());
        inventory.setItem(2, Flamethrower.create());
        inventory.setItem(3, RocketCrossbow.create());
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void handle(Player player, int slot) {
        switch (slot) {
            case 0 -> player.getInventory().addItem(SwordOfCrushing.create());
            case 1 -> player.getInventory().addItem(EnderSword.create());
            case 2 -> player.getInventory().addItem(Flamethrower.create());
            case 3 -> player.getInventory().addItem(RocketCrossbow.create());
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}