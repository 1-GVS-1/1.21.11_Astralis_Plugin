package org.Thegvsss.Astralis.items.relics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.Thegvsss.Astralis.items.ItemsRegister;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class GravityFeather {

    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.fromString("astralis:gravity_feather_model"));
        NamespacedKey key = new NamespacedKey("astralis", "gravity_feather");

        meta.getPersistentDataContainer().set(
                key,
                org.bukkit.persistence.PersistentDataType.INTEGER,
                1
        );
        meta.displayName(Component.text("Gravity Feather")
                .color(NamedTextColor.GOLD));
        meta.lore(List.of(
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
