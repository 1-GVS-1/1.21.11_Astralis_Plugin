package org.thegvsss.astralis.abilities;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.thegvsss.astralis.utils.CooldownManager;
import org.thegvsss.astralis.utils.ItemUtils;

/**
 * DragonSword
 * Passiva: ao atacar, empurra todos os inimigos próximos (raio 5).
 * Ativa: não há clique-direito — a rajada dispara automaticamente no hit,
 *        com cooldown de 30s. Dano extra de +20% no hit que aciona a habilidade.
 */
public class DragonSwordAbility implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        if (!(e.getEntity() instanceof LivingEntity)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemUtils.hasTag(item, "dragon_sword")) return;

        // Passiva sempre: sempre tem knockback extra no alvo direto
        Entity target = e.getEntity();
        Vector dir = target.getLocation().subtract(player.getLocation()).toVector().normalize();
        target.setVelocity(dir.multiply(1.2).setY(0.4));

        // Ativa (cooldown 30s): rajada de vento em AoE
        if (CooldownManager.isOnCooldown(player, "dragon_sword")) return;

        // +20% de dano extra no hit que ativa
        e.setDamage(e.getDamage() * 1.2);

        // Empurra todos ao redor
        for (Entity nearby : player.getNearbyEntities(5, 3, 5)) {
            if (!(nearby instanceof LivingEntity le)) continue;
            if (nearby.equals(player) || nearby.equals(target)) continue;

            Vector push = nearby.getLocation().subtract(player.getLocation()).toVector().normalize();
            nearby.setVelocity(push.multiply(1.5).setY(0.5));
            if (le instanceof Player p) {
                p.sendActionBar(Component.text("§6💨 Rajada do Dragão!"));
            }
        }

        // FX
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH,
                player.getLocation().add(0, 1, 0), 40, 1.5, 0.5, 1.5, 0.05);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.7f, 1.2f);
        player.sendActionBar(Component.text("§6🐉 Rajada do Dragão! Cooldown: §f30s"));

        CooldownManager.setCooldown(player, "dragon_sword", 30);
    }
}
