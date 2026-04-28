package org.thegvsss.astralis.items.relics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Dharma {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.fromString("astralis:dharma_model"));
        meta.displayName(Component.text("Dharma").color(NamedTextColor.GOLD));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "dharma"), PersistentDataType.INTEGER, 1);
        meta.lore(List.of(
                Component.text("§7Adapta-se ao tipo de dano recebido"),
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
