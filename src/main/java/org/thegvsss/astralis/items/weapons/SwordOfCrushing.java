package org.thegvsss.astralis.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class SwordOfCrushing {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Sword of Crushing").color(NamedTextColor.GOLD));
        meta.setItemModel(NamespacedKey.fromString("astralis:sword_of_crushing_model"));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "sword_of_crushing"), PersistentDataType.INTEGER, 1);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(), "dmg", 9, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.ATTACK_SPEED,
                new AttributeModifier(UUID.randomUUID(), "spd", -3, AttributeModifier.Operation.ADD_NUMBER));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.lore(List.of(
                Component.text("§7Dano: §f+12  §7Velocidade: §f1"),
                Component.text("§7Clique direito §f→ §7AoE paralisa"),
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
