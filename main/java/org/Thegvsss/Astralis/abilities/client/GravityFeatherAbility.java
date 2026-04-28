package org.Thegvsss.Astralis.abilities.client;

import org.Thegvsss.Astralis.Astralis;
import org.Thegvsss.Astralis.utils.CooldownManager;
import org.Thegvsss.Astralis.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GravityFeatherAbility implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!ItemUtils.hasTag(item, "gravity_feather")) return;

        if (CooldownManager.isOnCooldown(player, "gravity_feather")) return;

        player.setAllowFlight(true);
        player.setFlying(true);


        Bukkit.getScheduler().runTaskLater(Astralis.getInstance(), () -> {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage("§cSeu voo acabou!");
        }, 20 * 60);

        CooldownManager.setCooldown(player, "gravity_feather", 120);
    }
}