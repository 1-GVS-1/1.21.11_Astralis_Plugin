package org.thegvsss.astralis.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class HellSword {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Hell Sword").color(NamedTextColor.DARK_RED));
        meta.setItemModel(NamespacedKey.fromString("astralis:hell_sword_model"));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "hell_sword"), PersistentDataType.INTEGER, 1);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(), "dmg", 13, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.ATTACK_SPEED,
                new AttributeModifier(UUID.randomUUID(), "spd", -3.2, AttributeModifier.Operation.ADD_NUMBER));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.lore(List.of(
                Component.text("§7Forjada no fogo eterno do Nether."),
                Component.text("§6Habilidade: §fBarreira de fogo ao redor"),
                Component.text("§7Inimigos que tocam §cqueimam §7por 5s"),
                Component.text("§7Cooldown: §e60s"),
                Component.text("§7Dano: §f+13"),
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
