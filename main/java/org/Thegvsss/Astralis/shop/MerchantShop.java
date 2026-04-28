package org.Thegvsss.Astralis.shop;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class MerchantShop {

    public static Inventory create() {

        Inventory inv = Bukkit.createInventory(null, 27, "§6Astralis Shop");

        inv.setItem(10, ShopItems.unbreakable());
        inv.setItem(13, ShopItems.damage());
        inv.setItem(16, ShopItems.enchant());

        return inv;
    }
}