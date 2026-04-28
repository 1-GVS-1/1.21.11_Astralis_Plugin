package org.thegvsss.astralis.items.weapons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.thegvsss.astralis.utils.ItemUtils;

public class BlastTrident {

    private static final String NAME = "§bBlast Trident";
    private static final String TAG = "blast_trident";

    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.TRIDENT);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(NAME);
        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        // ⚠️ seu ItemUtils exige 3 parâmetros
        ItemUtils.setTag(item, TAG, 1);

        return item;
    }

    public static boolean isBlastTrident(ItemStack item) {
        return ItemUtils.hasTag(item, TAG);
    }
}