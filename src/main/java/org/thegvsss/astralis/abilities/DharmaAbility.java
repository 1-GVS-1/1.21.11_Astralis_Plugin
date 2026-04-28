package org.thegvsss.astralis.abilities;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.thegvsss.astralis.events.global.EffectsRelics;
import org.thegvsss.astralis.utils.ItemUtils;

import java.util.*;

/**
 * DharmaAbility — adapta-se ao tipo de dano recebido.
 *
 * BUG CORRIGIDO:
 *  - EffectsRelics.playAdaptEffects() agora é chamado corretamente.
 *  - Import net.kyori.adventure removido do import não-usado.
 *  - BossBar inicia com 0 progress (não ficava vazio).
 */
public class DharmaAbility implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, Integer> damageLevels = new HashMap<>();
    private final Map<UUID, String> damageType = new HashMap<>();
    private final Map<UUID, Long> lastDamageTime = new HashMap<>();
    private final Map<UUID, BossBar> bossBar = new HashMap<>();
    private final Map<UUID, Boolean> fullAdaptation = new HashMap<>();

    public DharmaAbility(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHurt(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        UUID id = player.getUniqueId();
        ItemStack helmet = player.getEquipment().getHelmet();

        // Se não usa Dharma, limpa estado
        if (!ItemUtils.hasTag(helmet, "dharma")) {
            removeBossBar(player);
            resetPlayer(id);
            return;
        }

        String type = getDamageType(e);
        long now = System.currentTimeMillis();

        // Reset de stack se mudou tipo de dano ou passou 5s
        if (!type.equals(damageType.get(id)) || now - lastDamageTime.getOrDefault(id, 0L) > 5000) {
            damageLevels.put(id, 0);
            fullAdaptation.remove(id);
        }

        int level = damageLevels.getOrDefault(id, 0) + 1;
        damageLevels.put(id, level);
        damageType.put(id, type);
        lastDamageTime.put(id, now);

        updateBossBar(player, type, level);
        applyEffects(player, type, level, e);
    }

    // Cancelar dano se totalmente adaptado
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageImmune(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        UUID id = player.getUniqueId();
        if (!fullAdaptation.getOrDefault(id, false)) return;

        String currentType = damageType.get(id);
        String incomingType = getDamageType(e);

        if (currentType != null && currentType.equals(incomingType)) {
            e.setCancelled(true);
            // Nega knockback
            player.setVelocity(player.getVelocity().multiply(0));
            player.sendActionBar(Component.text("§c⚡ Imune a §f" + currentType));
        }
    }

    private void updateBossBar(Player player, String type, int level) {
        String text = "§aDharma — Adaptação: §f" + type + " §7[" + level + "/25]";
        BossBar bar = bossBar.get(player.getUniqueId());

        if (bar == null) {
            bar = Bukkit.createBossBar(text, BarColor.GREEN, BarStyle.SEGMENTED_10);
            bar.setProgress(0.04); // inicia visível
            bar.addPlayer(player);
            bossBar.put(player.getUniqueId(), bar);
        } else {
            bar.setTitle(text);
            bar.setProgress(Math.min(level / 25.0, 1.0));
        }
    }

    private void removeBossBar(Player player) {
        BossBar bar = bossBar.remove(player.getUniqueId());
        if (bar != null) bar.removeAll();
    }

    private void applyEffects(Player player, String type, int level, EntityDamageEvent e) {
        UUID id = player.getUniqueId();

        if (level >= 25 && !fullAdaptation.getOrDefault(id, false)) {
            activateFullAdaptation(player, type);
        } else if (level >= 15) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
        } else if (level >= 10) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 0));
        }
    }

    private void activateFullAdaptation(Player player, String type) {
        UUID id = player.getUniqueId();
        fullAdaptation.put(id, true);

        // Efeitos visuais
        EffectsRelics.playAdaptEffects(player);

        player.sendActionBar(Component.text("§c§lADAPTAÇÃO COMPLETA: §f" + type));
        player.sendMessage("§6[Dharma] §aAdaptação total a §f" + type + "§a por 20s!");

        // Após 20s: exaustão pós-adaptação
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) return;
            fullAdaptation.remove(id);
            damageLevels.remove(id);
            damageType.remove(id);
            removeBossBar(player);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 4));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 4));
            player.sendMessage("§c[Dharma] §7Adaptação esgotada. Recuperando...");
        }, 20 * 20L);
    }

    private String getDamageType(EntityDamageEvent e) {
        return switch (e.getCause()) {
            case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> "Melee";
            case PROJECTILE -> "Projectile";
            case MAGIC, WITHER, DRAGON_BREATH -> "Magic";
            case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> "Explosion";
            case FIRE, FIRE_TICK, LAVA -> "Fire";
            case FALL -> "Fall";
            case POISON -> "Poison";
            default -> "Physical";
        };
    }

    private void resetPlayer(UUID id) {
        damageLevels.remove(id);
        damageType.remove(id);
        lastDamageTime.remove(id);
        fullAdaptation.remove(id);
    }
}
