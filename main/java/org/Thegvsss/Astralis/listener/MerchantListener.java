package org.Thegvsss.Astralis.listener;

import org.Thegvsss.Astralis.shop.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class MerchantListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {

        if (!e.getRightClicked().getScoreboardTags().contains("merchant")) return;

        Player p = e.getPlayer();

        e.setCancelled(true);
        p.openInventory(MerchantShop.create());
    }
}