package org.Thegvsss.Astralis.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class AstralisGui implements InventoryHolder {

    public static final String TITLE = "§4Astralis Menu";
    private Inventory inventory;

    public AstralisGui() {
        this.inventory = Bukkit.createInventory(this, 9, TITLE);

        inventory.setItem(2, new org.Thegvsss.Astralis.utils.gui.ItemBuilder(Material.NETHERITE_SWORD)
                .setName("§cWeapons")
                .toItemStack());

        inventory.setItem(4, new org.Thegvsss.Astralis.utils.gui.ItemBuilder(Material.TOTEM_OF_UNDYING)
                .setName("§6Relics")
                .toItemStack());

        inventory.setItem(6, new org.Thegvsss.Astralis.utils.gui.ItemBuilder(Material.EMERALD)
                .setName("§aMisc")
                .toItemStack());
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void handle(Player player, int slot) {
        switch (slot) {
            case 2 -> new org.Thegvsss.Astralis.utils.gui.WeaponsGui().open(player);
            case 4 -> new org.Thegvsss.Astralis.utils.gui.RelicsGui().open(player);
            case 6 -> new org.Thegvsss.Astralis.utils.gui.MiscGui().open(player);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}