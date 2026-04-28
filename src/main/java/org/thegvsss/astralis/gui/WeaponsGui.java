package org.thegvsss.astralis.gui;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.thegvsss.astralis.items.weapons.*;

public class WeaponsGui implements InventoryHolder {

    public static final String TITLE = "§cWeapons";
    private final Inventory inventory;

    public WeaponsGui() {
        inventory = Bukkit.createInventory(this, 9, TITLE);
        inventory.setItem(0, SwordOfCrushing.create());
        inventory.setItem(1, EnderSword.create());
        inventory.setItem(2, Flamethrower.create());
        inventory.setItem(3, RocketCrossbow.create());
        inventory.setItem(4, DragonSword.create());
        inventory.setItem(5, HellSword.create());
    }

    public void open(Player player) { player.openInventory(inventory); }

    public void handle(Player player, int slot) {
        switch (slot) {
            case 0 -> player.getInventory().addItem(SwordOfCrushing.create());
            case 1 -> player.getInventory().addItem(EnderSword.create());
            case 2 -> player.getInventory().addItem(Flamethrower.create());
            case 3 -> player.getInventory().addItem(RocketCrossbow.create());
            case 4 -> player.getInventory().addItem(DragonSword.create());
            case 5 -> player.getInventory().addItem(HellSword.create());
        }
    }

    @Override
    public Inventory getInventory() { return inventory; }
}
