package org.Thegvsss.Astralis.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtils {

    public static boolean hasTag(ItemStack item, String keyName) {
        if (item == null || !item.hasItemMeta()) return false;

        NamespacedKey key = new NamespacedKey("astralis", keyName);

        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(key, PersistentDataType.INTEGER);
    }
}