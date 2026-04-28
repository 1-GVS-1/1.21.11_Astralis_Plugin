package org.thegvsss.astralis.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
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
                new NamespacedKey("astralis", "flamethrower"), PersistentDataType.INTEGER, 1);
        meta.lore(List.of(
                Component.text("§5Dispara chamas na direção do olhar"),
                Component.text("§7Consome §fCarvão §7como combustível"),
                Component.text("§7Dano: §f1.5/tick §7| Fogo: §f3s"),
                Component.text("§aPode ser melhorada")
        ));
        item.setItemMeta(meta);
        return item;
    }
}
