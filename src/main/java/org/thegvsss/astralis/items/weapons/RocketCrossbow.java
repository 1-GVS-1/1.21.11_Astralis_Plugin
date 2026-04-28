package org.thegvsss.astralis.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class RocketCrossbow {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.CROSSBOW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Rocket Crossbow").color(NamedTextColor.GOLD));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "rocket_crossbow"), PersistentDataType.INTEGER, 1);
        meta.lore(List.of(
                Component.text("§5Dispara flechas-foguete"),
                Component.text("§7Explode no impacto e §flança §7alvos pro alto"),
                Component.text("§7Dano: §f8 (decresce com distância)"),
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
