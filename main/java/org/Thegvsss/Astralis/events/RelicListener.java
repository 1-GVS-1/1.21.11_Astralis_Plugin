
package org.thegvsss.astralis.events;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.*;

public class RelicListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if(item == null || !item.hasItemMeta()) return;

        String name = item.getItemMeta().getDisplayName();

        if(name.contains("Relic of Speed")){
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
        }

        if(name.contains("Relic of Power")){
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1));
        }
    }
}
