package org.thegvsss.astralis.items.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Coin {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.fromString("astralis:coin_model"));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "coin"), PersistentDataType.INTEGER, 1);
        meta.displayName(Component.text("Coin").color(NamedTextColor.GOLD));
        item.setItemMeta(meta);
        return item;
    }
}
