package org.Thegvsss.Astralis.shop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopItems {

    public static ItemStack coin(int amount) {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET, amount);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§6Coin");
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack coin() {
        return coin(1);
    }

    public static ItemStack unbreakable() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§eUnbreakable Book");

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack damage() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§c+30% Damage Book");

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack enchant() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§a+1 All Enchants Book");

        item.setItemMeta(meta);
        return item;
    }
}