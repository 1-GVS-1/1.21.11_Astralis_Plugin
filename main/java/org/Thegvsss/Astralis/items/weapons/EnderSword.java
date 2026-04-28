package org.Thegvsss.Astralis.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class EnderSword {

    public static ItemStack create() {

        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.fromString("astralis:ender_sword_model"));
        meta.displayName(Component.text("Ender Sword")
                .color(NamedTextColor.GOLD));

        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "ender_sword"),
                PersistentDataType.INTEGER,
                1
        );
        meta.addAttributeModifier(
                Attribute.ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(), "damage", 9, AttributeModifier.Operation.ADD_NUMBER)


        );
        meta.setUnbreakable(true);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        meta.lore(List.of(
                Component.text("§7Damage: §f+9"),
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));

        item.setItemMeta(meta);
        return item;
    }
}