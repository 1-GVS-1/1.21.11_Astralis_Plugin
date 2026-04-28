package org.Thegvsss.Astralis.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
                new NamespacedKey("astralis", "rocket_crossbow"),
                PersistentDataType.INTEGER,
                1
        );

        meta.lore(List.of(
                Component.text("§5Dispara foguetes"),
                Component.text("§7Lança inimigos para o alto")
        ));

        item.setItemMeta(meta);
        return item;
    }
}