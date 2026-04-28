package org.Thegvsss.Astralis.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Flamethrower {

    public static ItemStack create() {

        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("Flamethrower").color(NamedTextColor.GOLD));

        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "flamethrower"),
                PersistentDataType.INTEGER,
                1
        );

        meta.lore(List.of(
                Component.text("§5Dispara fogo continuamente"),
                Component.text("§7Consome carvão"),
                Component.text("§aPode ser melhorada")
        ));

        item.setItemMeta(meta);
        return item;
    }
}