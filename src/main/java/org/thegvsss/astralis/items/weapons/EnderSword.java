package org.thegvsss.astralis.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class EnderSword {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.fromString("astralis:ender_sword_model"));
        meta.displayName(Component.text("Ender Sword").color(NamedTextColor.GOLD));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "ender_sword"), PersistentDataType.INTEGER, 1);

        new AttributeModifier(
                new NamespacedKey("astralis", "damage"),
                9,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.MAINHAND
        );

        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.lore(List.of(
                Component.text("§7Dano: §f+9"),
                Component.text("§7Clique direito §f→ §7Teleporte"),
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
