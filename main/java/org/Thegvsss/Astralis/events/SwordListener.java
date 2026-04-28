
package org.thegvsss.astralis.events;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.*;
import org.bukkit.util.Vector;
import org.thegvsss.astralis.utils.CooldownManager;

public class SwordListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player p)) return;
        if(!(e.getEntity() instanceof LivingEntity t)) return;

        ItemStack item = p.getInventory().getItemInMainHand();
        if(item == null || !item.hasItemMeta()) return;

        String name = item.getItemMeta().getDisplayName();

        if(name.contains("Crushing")){
            if(CooldownManager.onCooldown(p)) return;
            CooldownManager.set(p, 5);

            t.setVelocity(new Vector(0,0,0));
            t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60,255));
            t.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,60,1));
        }

        if(name.contains("Flame")){
            t.setFireTicks(100);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if(item == null || !item.hasItemMeta()) return;

        String name = item.getItemMeta().getDisplayName();

        if(name.contains("Crushing")){
            if(CooldownManager.onCooldown(p)) return;
            CooldownManager.set(p, 8);

            p.getWorld().createExplosion(p.getLocation(), 2F);
        }
    }
}
