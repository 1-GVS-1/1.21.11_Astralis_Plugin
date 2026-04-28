package org.Thegvsss.Astralis.utils.gui;

import org.Thegvsss.Astralis.items.misc.Coin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MiscGui implements InventoryHolder {

    public static final String TITLE = "§aMisc";
    private final Inventory inventory;

    public MiscGui() {
        inventory = Bukkit.createInventory(this, 9, TITLE);
        inventory.setItem(0, new Coin().create());
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void handle(Player player, int slot) {
        if (slot == 0) player.getInventory().addItem(new Coin().create());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}