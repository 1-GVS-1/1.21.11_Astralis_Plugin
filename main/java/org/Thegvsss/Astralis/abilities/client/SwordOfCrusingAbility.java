package org.Thegvsss.Astralis.abilities.weapons;

import org.Thegvsss.Astralis.Astralis;
import org.Thegvsss.Astralis.utils.CooldownManager;
import org.Thegvsss.Astralis.utils.ItemUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SwordOfCrusingAbility implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!ItemUtils.hasTag(item, "sword_of_crushing")) return;

        if (CooldownManager.isOnCooldown(player, "sword_of_crushing")) return;

        LivingEntity target = getTarget(player, 3);

        if (target == null) {
            player.sendMessage("§cSem alvo!");
            return;
        }

        // 🔥 efeitos pesados
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 0.5f);

        player.getWorld().spawnParticle(
                Particle.EXPLOSION,
                target.getLocation().add(0, 1, 0),
                1
        );

        player.getWorld().spawnParticle(
                Particle.SQUID_INK,
                target.getLocation(),
                40,
                0.5, 1, 0.5,
                0.1
        );

        // 👁️ cegueira + escuridão
        target.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 60, 1));

        // 🧊 paralisa
        freeze(target, 60);

        // ⏳ cooldown 60s
        CooldownManager.setCooldown(player, "sword_of_crushing", 60);
    }

    private void freeze(LivingEntity entity, int ticks) {

        entity.setAI(false);

        Bukkit.getScheduler().runTaskLater(
                Astralis.getInstance(),
                () -> entity.setAI(true),
                ticks
        );
    }

    private LivingEntity getTarget(Player player, double range) {

        for (Entity entity : player.getNearbyEntities(range, range, range)) {

            if (!(entity instanceof LivingEntity living)) continue;
            if (entity.equals(player)) continue;

            return living;
        }

        return null;
    }
}