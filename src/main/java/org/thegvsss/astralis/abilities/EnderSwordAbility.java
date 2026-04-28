package org.thegvsss.astralis.abilities;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.thegvsss.astralis.utils.CooldownManager;
import org.thegvsss.astralis.utils.ItemUtils;

public class EnderSwordAbility implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (!ItemUtils.hasTag(item, "ender_sword")) return;
        if (CooldownManager.isOnCooldown(p, "ender_sword")) return;

        e.setCancelled(true);

        // BUG CORRIGIDO: getTargetBlockExact pode retornar null
        Block target = p.getTargetBlockExact(15);

        if (target == null) {
            p.sendMessage("§cNenhum bloco no alcance para teleportar!");
            return;
        }

        Location dest = target.getLocation().add(0.5, 1, 0.5);

        // Garante que não teleporta dentro de bloco sólido
        if (dest.getBlock().getType().isSolid()
                || dest.clone().add(0, 1, 0).getBlock().getType().isSolid()) {
            p.sendMessage("§cBloco obstruído! Tente outro local.");
            return;
        }

        // FX de saída
        p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 30, 0.3, 1, 0.3, 0.1);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        p.teleport(dest);

        // FX de chegada
        p.getWorld().spawnParticle(Particle.PORTAL, dest, 30, 0.3, 1, 0.3, 0.1);
        p.sendActionBar(Component.text("§5⚡ Teletransporte!"));

        CooldownManager.setCooldown(p, "ender_sword", 5);
    }
}
