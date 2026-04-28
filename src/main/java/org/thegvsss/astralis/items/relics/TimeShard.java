package org.thegvsss.astralis.items.relics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class TimeShard {
    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.fromString("astralis:time_shard_model"));
        meta.displayName(Component.text("Time Shard").color(NamedTextColor.LIGHT_PURPLE));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "time_shard"), PersistentDataType.INTEGER, 1);
        meta.lore(List.of(
                Component.text("§7Um fragmento do tempo quebrado."),
                Component.text("§6Poder: §fParalisa inimigos próximos"),
                Component.text("§7Você fica §facelerado §7enquanto eles param"),
                Component.text("§7Raio: §e10 blocos §7| Cooldown: §e60s"),
                Component.text("Pode Ser Melhorada").color(NamedTextColor.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
