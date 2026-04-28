package org.thegvsss.astralis.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.thegvsss.astralis.AstralisCore;

public class AstralisGui implements InventoryHolder {

    public static final String TITLE = "§4§lAstralis Menu";
    private final Inventory inventory;
    private final AstralisCore plugin;

    public AstralisGui(AstralisCore plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(this, 9, TITLE);

        inventory.setItem(1, icon(Material.NETHERITE_SWORD, "§cWeapons", "§7Ver armas especiais"));
        inventory.setItem(3, icon(Material.TOTEM_OF_UNDYING, "§6Relics", "§7Ver relíquias"));
        inventory.setItem(5, icon(Material.GOLD_NUGGET, "§eEconomia & Loja", "§7Coins, loja e buffs"));
        inventory.setItem(7, icon(Material.WRITABLE_BOOK, "§bQuests", "§7Progresso das quests de relíquias"));
    }

    private ItemStack icon(Material mat, String name, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.lore(java.util.List.of(Component.text(lore)));
        item.setItemMeta(meta);
        return item;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void handle(Player player, int slot) {
        switch (slot) {
            case 1 -> new WeaponsGui().open(player);
            case 3 -> new RelicsGui().open(player);
            case 5 -> player.openInventory(org.thegvsss.astralis.shop.MerchantShopListener.create());
            case 7 -> plugin.getQuestManager().showProgress(player);
        }
    }

    @Override
    public Inventory getInventory() { return inventory; }
}
