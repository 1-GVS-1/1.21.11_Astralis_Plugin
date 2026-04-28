package org.Thegvsss.Astralis.shop;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MerchantShopListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;

        if (!e.getView().getTitle().equals("§5Astralis Shop")) return;

        e.setCancelled(true);

        int slot = e.getSlot();
        int price = 10;

        if (!hasCoins(p, price)) {
            p.sendMessage("§cSem coins!");
            return;
        }

        removeCoins(p, price);

        switch (slot) {
            case 0 -> p.getInventory().addItem(ShopItems.unbreakable());
            case 1 -> p.getInventory().addItem(ShopItems.damage());
            case 2 -> p.getInventory().addItem(ShopItems.enchant());
        }

        p.sendMessage("§aCompra realizada!");
    }

    private boolean hasCoins(Player p, int amount) {
        return p.getInventory().containsAtLeast(ShopItems.coin(), amount);
    }

    private void removeCoins(Player p, int amount) {
        p.getInventory().removeItem(ShopItems.coin(amount));
    }
}