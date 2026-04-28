package org.thegvsss.astralis.items.relics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ForbidenTotem {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.fromString("astralis:forbiden_totem_model"));
        meta.displayName(Component.text("Forbiden Totem").color(NamedTextColor.DARK_PURPLE));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "forbiden_totem"), PersistentDataType.INTEGER, 1);
        meta.setUnbreakable(true);
        meta.lore(List.of(
                Component.text("§7Relíquia maldita do End."),
                Component.text("§6Poder: §fImortalidade extrema"),
                Component.text("§7Ao receber dano fatal: §fsobrevive com §c1♥"),
                Component.text("§7e fica invencível por §f10s"),
                Component.text("§7Cooldown: §e60s"),
                Component.text("§cPreço a ser cobrado..."),
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
