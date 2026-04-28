package org.thegvsss.astralis.utils;

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

    public static void setTag(ItemStack item, String keyName, int value) {
        if (item == null || !item.hasItemMeta()) return;
        var meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", keyName),
                PersistentDataType.INTEGER, value
        );
        item.setItemMeta(meta);
    }

    public static int getTag(ItemStack item, String keyName) {
        if (item == null || !item.hasItemMeta()) return -1;
        NamespacedKey key = new NamespacedKey("astralis", keyName);
        Integer val = item.getItemMeta().getPersistentDataContainer()
                .get(key, PersistentDataType.INTEGER);
        return val != null ? val : -1;
    }
}
