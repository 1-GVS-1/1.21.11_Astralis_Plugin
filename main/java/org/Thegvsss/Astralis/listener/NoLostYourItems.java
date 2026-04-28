package org.Thegvsss.Astralis.utils.listener;

import org.Thegvsss.Astralis.items.ItemsRegister;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

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