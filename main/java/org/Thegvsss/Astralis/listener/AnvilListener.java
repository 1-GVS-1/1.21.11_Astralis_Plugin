package org.Thegvsss.Astralis.utils.listener;

import org.Thegvsss.Astralis.utils.ItemUtils;
import org.bukkit.event.*;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilListener implements Listener {

    @EventHandler
    public void onAnvil(PrepareAnvilEvent e) {

        ItemStack base = e.getInventory().getItem(0);
        ItemStack book = e.getInventory().getItem(1);

        if (base == null || book == null) return;

        ItemStack result = base.clone();

        // 📕 UNBREAKABLE
        if (ItemUtils.hasTag(book, "book_unbreakable")) {

            var meta = result.getItemMeta();
            meta.setUnbreakable(true);
            result.setItemMeta(meta);

            e.setResult(result);
        }

        // 📕 +30% DAMAGE
        if (ItemUtils.hasTag(book, "book_damage")) {

            var meta = result.getItemMeta();

            meta.addAttributeModifier(
                    org.bukkit.attribute.Attribute.ATTACK_DAMAGE,
                    new org.bukkit.attribute.AttributeModifier(
                            java.util.UUID.randomUUID(),
                            "boost",
                            0.3,
                            org.bukkit.attribute.AttributeModifier.Operation.MULTIPLY_SCALAR_1
                    )
            );

            result.setItemMeta(meta);
            e.setResult(result);
        }

        // 📕 +1 ENCHANT
        if (ItemUtils.hasTag(book, "book_enchant")) {

            var meta = result.getItemMeta();

            result.getEnchantments().forEach((ench, lvl) -> {
                result.addUnsafeEnchantment(ench, lvl + 1);
            });

            result.setItemMeta(meta);
            e.setResult(result);
        }
    }
}