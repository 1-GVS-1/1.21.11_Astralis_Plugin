package org.thegvsss.astralis.items;

import org.bukkit.inventory.ItemStack;
import org.thegvsss.astralis.utils.ItemUtils;

public class ItemsRegister {
    public static boolean isRegistered(ItemStack item) {
        return ItemUtils.hasTag(item, "dharma")
                || ItemUtils.hasTag(item, "gravity_feather")
                || ItemUtils.hasTag(item, "sword_of_crushing")
                || ItemUtils.hasTag(item, "ender_sword")
                || ItemUtils.hasTag(item, "flamethrower")
                || ItemUtils.hasTag(item, "rocket_crossbow")
                || ItemUtils.hasTag(item, "coin")
                // Novos itens
                || ItemUtils.hasTag(item, "dragon_sword")
                || ItemUtils.hasTag(item, "hell_sword")
                || ItemUtils.hasTag(item, "forbiden_totem")
                || ItemUtils.hasTag(item, "phoenix_feather")
                || ItemUtils.hasTag(item, "time_shard");
    }
}
