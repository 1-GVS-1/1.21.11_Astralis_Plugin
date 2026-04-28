package org.thegvsss.astralis.listener;

import org.bukkit.GameMode;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.thegvsss.astralis.items.ItemsRegister;

public class NoLostYourItems implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!ItemsRegister.isRegistered(e.getItemInHand())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (!ItemsRegister.isRegistered(e.getItemDrop().getItemStack())) return;
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        e.setCancelled(true);
    }
}
