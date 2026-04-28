package org.thegvsss.astralis.items.relics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class PhoenixFeather {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.fromString("astralis:phoenix_feather_model"));
        meta.displayName(Component.text("Phoenix Feather").color(NamedTextColor.GOLD));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "phoenix_feather"), PersistentDataType.INTEGER, 1);
        meta.lore(List.of(
                Component.text("§7Cinzas que nunca morrem."),
                Component.text("§6Poder: §fRevive ao morrer"),
                Component.text("§7Ao morrer: §fresurge com §a50% §7de vida"),
                Component.text("§7com efeito de §fRegeneração §7por 5s"),
                Component.text("§7Cooldown: §e300s §7(5 min)"),
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
