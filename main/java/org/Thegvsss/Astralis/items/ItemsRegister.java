package org.Thegvsss.Astralis.items;

import org.Thegvsss.Astralis.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

public class ItemsRegister {

    public static boolean isRegistered(ItemStack item) {

        return ItemUtils.hasTag(item, "dharma")
                || ItemUtils.hasTag(item, "sword_of_crushing")
                || ItemUtils.hasTag(item, "gravity_feather");
    }
}