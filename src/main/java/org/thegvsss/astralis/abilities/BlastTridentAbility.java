package org.thegvsss.astralis.abilities;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.thegvsss.astralis.items.weapons.BlastTrident;
import org.thegvsss.astralis.utils.CooldownManager;

public class BlastTridentAbility implements Listener {

    private final JavaPlugin plugin;

    public BlastTridentAbility(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // 🚀 uso principal
    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (!BlastTrident.isBlastTrident(item)) return;
        if (CooldownManager.isOnCooldown(p, "blast_trident")) return;

        e.setCancelled(true);

        Vector vel = p.getLocation().getDirection().normalize().multiply(2.8);

        p.setVelocity(vel);
        p.setFallDistance(0);

        World w = p.getWorld();
        w.playSound(p.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_3, 1, 1);
        w.spawnParticle(Particle.EXPLOSION, p.getLocation(), 2);

        CooldownManager.setCooldown(p, "blast_trident", 3);
    }

    // 💨 partículas segurando
    @EventHandler
    public void onHold(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!p.isOnline()) {
                    cancel();
                    return;
                }

                ItemStack item = p.getInventory().getItemInMainHand();

                if (!BlastTrident.isBlastTrident(item)) {
                    cancel();
                    return;
                }

                p.getWorld().spawnParticle(
                        Particle.SMOKE,
                        p.getLocation().add(0, 1, 0),
                        5,
                        0.2, 0.2, 0.2,
                        0.02
                );
            }
        }.runTaskTimer(plugin, 0, 5);
    }
}