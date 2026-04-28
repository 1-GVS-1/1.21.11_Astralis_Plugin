package org.Thegvsss.Astralis.abilities.client;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FlamethrowerAbility implements Listener {

    private int fuel = 0;

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (!item.getType().equals(Material.BLAZE_ROD)) return;

        if (fuel <= 0) {
            if (!p.getInventory().contains(Material.COAL)) {
                p.sendMessage("§cSem combustível!");
                return;
            }

            p.getInventory().removeItem(new ItemStack(Material.COAL, 1));
            fuel = 20 * 20;
            p.sendMessage("§eCombustível carregado!");
        }

        fuel--;

        Location loc = p.getEyeLocation();
        Vector dir = loc.getDirection();

        for (int i = 0; i < 5; i++) {

            Location point = loc.clone().add(dir.clone().multiply(i));

            p.getWorld().spawnParticle(Particle.FLAME, point, 2);

            for (Entity en : p.getNearbyEntities(2, 2, 2)) {
                if (en instanceof LivingEntity le && !en.equals(p)) {
                    le.damage(2.0);
                    le.setFireTicks(40);
                    le.setVelocity(dir.clone().multiply(0.4));
                }
            }
        }
    }
}