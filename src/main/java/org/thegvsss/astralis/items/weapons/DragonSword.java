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

public class DragonSword {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Dragon Sword").color(NamedTextColor.GOLD));
        meta.setItemModel(NamespacedKey.fromString("astralis:dragon_sword_model"));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "dragon_sword"), PersistentDataType.INTEGER, 1);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(), "dmg", 11, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.ATTACK_SPEED,
                new AttributeModifier(UUID.randomUUID(), "spd", -2.8, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE,
                new AttributeModifier(UUID.randomUUID(), "range", 0.5, AttributeModifier.Operation.ADD_NUMBER));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.lore(List.of(
                Component.text("§7Forjada com o coração de um dragão ancestral."),
                Component.text("§7Seu poder distorce o próprio ar."),
                Component.text("§6Habilidade: §fRajada de vento ao atacar"),
                Component.text("§7Empurra inimigos próximos | §cDano extra: §f+20%"),
                Component.text("§7Cooldown: §e30s"),
                Component.text("§7Dano: §f+11  §7Velocidade: §f1.8  §7Alcance: §b+0.5"),
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
