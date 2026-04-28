package org.thegvsss.astralis.abilities;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.thegvsss.astralis.utils.CooldownManager;
import org.thegvsss.astralis.utils.ItemUtils;

/**
 * TimeShard
 * Ativa (clique direito): paralisa inimigos num raio de 10 blocos
 * por 4s (Slowness VI + MiningFatigue III + setAI false em mobs).
 * O jogador fica acelerado (Speed II) durante o mesmo período.
 * Cooldown: 60s.
 */
public class TimeShardAbility implements Listener {

    private final JavaPlugin plugin;

    public TimeShardAbility(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!ItemUtils.hasTag(item, "time_shard")) return;
        if (CooldownManager.isOnCooldown(player, "time_shard")) return;

        e.setCancelled(true);

        // Velocidade para o jogador
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 2, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 80, 1, false, false));

        // Paralisa inimigos no raio de 10 blocos
        boolean hit = false;
        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if (!(entity instanceof LivingEntity le)) continue;
            if (entity.equals(player)) continue;

            le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 80, 6, false, false));
            le.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 80, 3, false, false));
            le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false, false));

            if (!(le instanceof Player)) {
                le.setAI(false);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (le.isValid()) le.setAI(true);
                }, 80L);
            }

            // FX de cristal no alvo
            entity.getWorld().spawnParticle(Particle.END_ROD,
                    entity.getLocation().add(0, 1, 0), 20, 0.3, 0.5, 0.3, 0.05);
            hit = true;
        }

        // FX geral
        Location loc = player.getLocation();
        loc.getWorld().spawnParticle(Particle.END_ROD, loc.add(0, 1, 0), 80, 5, 1, 5, 0.02);
        loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 40, 5, 1, 5, 0.1);
        loc.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 0.5f);
        loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.5f, 1.5f);

        if (!hit) {
            player.sendMessage("§d[Time Shard] §7Nenhum inimigo no raio de 10 blocos.");
        }

        player.sendActionBar(Component.text("§d⏳ Tempo Parado! Cooldown: §f60s"));
        CooldownManager.setCooldown(player, "time_shard", 60);
    }
}
