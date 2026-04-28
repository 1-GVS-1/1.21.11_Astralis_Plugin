package org.thegvsss.astralis.abilities;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.thegvsss.astralis.utils.CooldownManager;
import org.thegvsss.astralis.utils.ItemUtils;

/**
 * SwordOfCrushing
 *
 * BUG CORRIGIDO:
 *  - setAI(false) não funciona em Players — agora verifica antes.
 *  - Funciona em qualquer LivingEntity.
 *  - Habilidade ativa no RIGHT CLICK (área AoE) e passiva no hit (slow + blindness).
 */
public class SwordOfCrushingAbility implements Listener {

    private final JavaPlugin plugin;

    public SwordOfCrushingAbility(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ─── Habilidade ativa: clique direito → AoE ────────────────────────────

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!ItemUtils.hasTag(item, "sword_of_crushing")) return;
        if (CooldownManager.isOnCooldown(player, "sword_of_crushing")) return;

        e.setCancelled(true);

        // AoE 4 blocos
        boolean hit = false;
        for (Entity entity : player.getNearbyEntities(4, 4, 4)) {
            if (!(entity instanceof LivingEntity le)) continue;
            if (entity.equals(player)) continue;

            // Efeitos
            applyEffects(player, le);
            le.damage(6.0, player);
            hit = true;
        }

        if (!hit) {
            player.sendMessage("§cNenhum alvo no alcance!");
            return;
        }

        // FX
        player.getWorld().spawnParticle(Particle.EXPLOSION, player.getLocation().add(0, 1, 0), 2);
        player.getWorld().spawnParticle(Particle.SQUID_INK, player.getLocation(), 40, 1, 1, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.8f, 0.5f);

        CooldownManager.setCooldown(player, "sword_of_crushing", 60);
        player.sendActionBar(net.kyori.adventure.text.Component.text("§cCrushing! Cooldown: §f60s"));
    }

    // ─── Passiva: ao dar hit normal → slow + blindness ─────────────────────

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        if (!(e.getEntity() instanceof LivingEntity target)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemUtils.hasTag(item, "sword_of_crushing")) return;

        if (CooldownManager.isOnCooldown(player, "sword_crushing_passive")) return;

        // Slow I por 1.5s em todo hit (sem cooldown da ativa)
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 30, 0, false, false));

        CooldownManager.setCooldown(player, "sword_crushing_passive", 2);
    }

    // ─── Helper: aplicar efeitos de paralisia ──────────────────────────────

    private void applyEffects(Player source, LivingEntity target) {
        // Blindness funciona em mobs também
        target.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 80, 1));
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 4));

        // setAI(false) só funciona em mobs (não em Player)
        if (!(target instanceof Player)) {
            target.setAI(false);
            org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (target.isValid()) target.setAI(true);
            }, 60L);
        } else {
            // Para players: aplica SLOW + MINING_FATIGUE como alternativa
            target.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 60, 10));
        }
    }
}
