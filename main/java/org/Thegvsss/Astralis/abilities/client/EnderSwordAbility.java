package org.Thegvsss.Astralis.abilities.weapons;

import org.Thegvsss.Astralis.utils.CooldownManager;
import org.Thegvsss.Astralis.utils.ItemUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EnderSwordAbility implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        if (!e.getAction().isRightClick()) return;

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (!ItemUtils.hasTag(item, "ender_sword")) return;

        if (CooldownManager.isOnCooldown(p, "ender_sword")) return;

        Block target = p.getTargetBlockExact(10);



        Location loc = target.getLocation().add(0.5, 1, 0.5);

        p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 20);
        p.teleport(loc);
        p.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        CooldownManager.setCooldown(p, "ender_sword", 3);
    }
}