package org.Thegvsss.Astralis.items.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Coin {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.fromString("astralis:coin_model"));
        NamespacedKey key = new NamespacedKey("astralis", "coin");

        meta.getPersistentDataContainer().set(
                key,
                org.bukkit.persistence.PersistentDataType.INTEGER,
                1
        );
        meta.displayName(Component.text("Coin")
                .color(NamedTextColor.GOLD));
        item.setItemMeta(meta);
        return item;
    }
}
