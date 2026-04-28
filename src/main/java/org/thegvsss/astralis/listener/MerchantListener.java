package org.thegvsss.astralis.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.thegvsss.astralis.shop.MerchantShopListener;

public class MerchantListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (!e.getRightClicked().getScoreboardTags().contains("merchant")) return;
        e.setCancelled(true);
        ((Player) e.getPlayer()).openInventory(MerchantShopListener.create());
    }
}
