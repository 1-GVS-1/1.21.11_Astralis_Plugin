package org.thegvsss.astralis.shop;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.thegvsss.astralis.economy.EconomyManager;
import org.thegvsss.astralis.utils.ItemUtils;

import java.util.*;

/**
 * Loja do Merchant — compras com Coins.
 *
 * Produtos:
 *  [Slot 10] 15 coins — Unbreakable Book (aplica em arma/relíquia no Anvil)
 *  [Slot 12] 20 coins — +30% Damage Book (aplica no Anvil)
 *  [Slot 14] 25 coins — +1 All Enchants Book (aplica no Anvil, além do max)
 *  [Slot 16] 30 coins — Speed I Permanente (efeito direto ao player)
 *  [Slot 20] 35 coins — Strength I Permanente (efeito direto ao player)
 *  [Slot 22] 40 coins — Regeneração Passiva (efeito direto ao player)
 *  [Slot 24] 50 coins — Proteção +5 (aplica diretamente na armadura)
 *  [Slot 31] 10 coins — Ver saldo
 */
public class MerchantShopListener implements Listener {

    private static final String TITLE = "§5§lAstralis Shop";
    private final EconomyManager economy;

    public MerchantShopListener(JavaPlugin plugin) {
        this.economy = plugin instanceof org.thegvsss.astralis.AstralisCore c
                ? c.getEconomyManager()
                : null;
    }

    public static Inventory create() {
        Inventory inv = Bukkit.createInventory(null, 36, TITLE);

        inv.setItem(10, makeShopItem(Material.ENCHANTED_BOOK, "§eUnbreakable Book", 15,
                "§7Aplica §fIndestrutível §7em qualquer item no Anvil"));
        inv.setItem(12, makeShopItem(Material.BOOK, "§c+30% Damage Book", 20,
                "§7Aplica §f+30% §7de dano no Anvil"));
        inv.setItem(14, makeShopItem(Material.WRITABLE_BOOK, "§a+1 All Enchants Book", 25,
                "§7Sobe §fTodos §7os encantos +1 (além do máximo!)"));
        inv.setItem(16, makeShopItem(Material.SUGAR, "§bSpeed I Permanente", 30,
                "§7Velocidade §fI §7permanente enquanto online"));
        inv.setItem(20, makeShopItem(Material.BLAZE_POWDER, "§cStrength I Permanente", 35,
                "§7Força §fI §7permanente enquanto online"));
        inv.setItem(22, makeShopItem(Material.GLISTERING_MELON_SLICE, "§aRegeneração Passiva", 40,
                "§7Regeneração §fpassiva §7ativada permanentemente"));
        inv.setItem(24, makeShopItem(Material.IRON_CHESTPLATE, "§9Proteção +5 (Armadura)", 50,
                "§7Aplica §f+5 §7de proteção na armadura equipada"));

        // Decoração
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta gm = glass.getItemMeta();
        gm.displayName(Component.text(" "));
        glass.setItemMeta(gm);
        for (int i = 0; i < 36; i++) {
            if (inv.getItem(i) == null) inv.setItem(i, glass);
        }

        return inv;
    }

    private static ItemStack makeShopItem(Material mat, String name, int price, String desc) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.lore(List.of(
                Component.text(desc),
                Component.text("§6Preço: §f" + price + " §6Coins")
        ));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", "shop_price"),
                PersistentDataType.INTEGER, price
        );
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (!e.getView().getTitle().equals(TITLE)) return;

        e.setCancelled(true);

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        // Slot do inventário do player (canto inferior) — ignorar
        if (e.getClickedInventory() != null && e.getClickedInventory().equals(p.getInventory())) {
            e.setCancelled(false);
            return;
        }

        Integer price = clicked.getItemMeta().getPersistentDataContainer()
                .get(new NamespacedKey("astralis", "shop_price"), PersistentDataType.INTEGER);
        if (price == null) return;

        if (economy == null || !economy.hasCoins(p, price)) {
            p.sendMessage("§cSem coins! Você tem §e" +
                    (economy != null ? economy.getBalance(p) : 0) + " §ccoins.");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.7f, 1f);
            return;
        }

        economy.removeCoins(p, price);

        switch (e.getSlot()) {
            case 10 -> giveBook(p, "book_unbreakable", "§eUnbreakable Book", Material.ENCHANTED_BOOK);
            case 12 -> giveBook(p, "book_damage", "§c+30% Damage Book", Material.BOOK);
            case 14 -> giveBook(p, "book_enchant", "§a+1 All Enchants Book", Material.WRITABLE_BOOK);
            case 16 -> applyPermanentEffect(p, PotionEffectType.SPEED, 0, "Speed I Permanente");
            case 20 -> applyPermanentEffect(p, PotionEffectType.STRENGTH, 0, "Strength I Permanente");
            case 22 -> applyPermanentEffect(p, PotionEffectType.REGENERATION, 0, "Regeneração Passiva");
            case 24 -> applyArmorProtection(p);
        }

        p.sendMessage("§aCompra realizada! §7Saldo restante: §e" + economy.getBalance(p) + " coins");
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.2f);
    }

    private void giveBook(Player p, String tag, String name, Material mat) {
        ItemStack book = new ItemStack(mat);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text(name));
        meta.getPersistentDataContainer().set(
                new NamespacedKey("astralis", tag), PersistentDataType.INTEGER, 1
        );
        book.setItemMeta(meta);
        p.getInventory().addItem(book);
    }

    private void applyPermanentEffect(Player p, PotionEffectType type, int amp, String name) {
        // 30 dias em ticks (~2.5 bilhões) = efeito "permanente" online
        p.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, amp, false, false, true));
        p.sendMessage("§a[Loja] §f" + name + " §aativado permanentemente!");
    }

    private void applyArmorProtection(Player p) {
        ItemStack chestplate = p.getInventory().getChestplate();
        if (chestplate == null || chestplate.getType() == Material.AIR) {
            p.sendMessage("§cVocê precisa estar usando um peitoral!");
            // Devolve coins
            if (economy != null) economy.addCoins(p, 50);
            return;
        }
        // Encanta além do máximo com unsafe
        int current = chestplate.getEnchantmentLevel(Enchantment.PROTECTION);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION, current + 5);
        p.sendMessage("§9[Loja] §fProteção §9+" + (current + 5) + " §fna armadura!");
    }
}
