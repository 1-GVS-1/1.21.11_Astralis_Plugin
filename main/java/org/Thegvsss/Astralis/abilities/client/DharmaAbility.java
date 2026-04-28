package org.Thegvsss.Astralis.abilities.client;

import net.kyori.adventure.text.Component;
import org.Thegvsss.Astralis.Astralis;
import org.Thegvsss.Astralis.events.global.EffectsRelics;
import org.Thegvsss.Astralis.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DharmaAbility implements Listener {

    private final Map<UUID, Integer> damageLevels = new HashMap<>();
    private final Map<UUID, String> damageType = new HashMap<>();
    private final Map<UUID, Long> lastDamageTime = new HashMap<>();
    private final Map<UUID, org.bukkit.boss.BossBar> bossBar = new HashMap<>();
    private final Map<UUID, Boolean> fullAdaptation = new HashMap<>();

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        UUID id = player.getUniqueId();

        ItemStack helmet = player.getEquipment().getHelmet();

        if (!ItemUtils.hasTag(helmet, "dharma")) {
            removeBossBar(player);
            resetPlayer(id);
            return;
        }
        String type = getDamageType(e);
        long now = System.currentTimeMillis();

        if (!type.equals(damageType.get(id)) || now - lastDamageTime.getOrDefault(id, 0L) > 5000) {
            damageLevels.put(id, 0);
        }

        int level = damageLevels.getOrDefault(id, 0) + 1;
        damageLevels.put(id, level);
        damageType.put(id, type);
        lastDamageTime.put(id, now);

        updateBossBar(player, type, level);
        applyEffects(player, type, level);

    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        UUID id = player.getUniqueId();

        if (!fullAdaptation.getOrDefault(id, false)) return;

        String currentType = damageType.get(id);
        String incomingType = getDamageType(e);

        if (currentType != null && currentType.equals(incomingType)) {
            e.setCancelled(true);
            player.setVelocity(player.getVelocity().zero());
        }
    }

    private void updateBossBar(Player player, String type, int level) {
        String text = "§aAdaptação: §f" + type + " §7[" + level + "]";
        var bar = bossBar.get(player.getUniqueId());

        if (bar == null) {
            bar = Bukkit.createBossBar(text, BarColor.GREEN, BarStyle.SOLID);
            bar.addPlayer(player);
            bossBar.put(player.getUniqueId(), bar);
        } else {
            bar.setTitle(text);
            bar.setProgress(Math.min(level / 25.0, 1.0));
        }
    }

    private void removeBossBar(Player player) {
        var bar = bossBar.remove(player.getUniqueId());
        if (bar != null) bar.removeAll();
    }

    private void applyEffects(Player player, String type, int level) {

        UUID id = player.getUniqueId();

        if (level >= 25 && !fullAdaptation.getOrDefault(id, false)) {
            activateFullAdaptation(player, type);
        } else if (level >= 15) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
        } else if (level >= 10) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 1));
        }
    }

    private void activateFullAdaptation(Player player, String type) {
        UUID id = player.getUniqueId();

        fullAdaptation.put(id, true);

        player.playSound(player.getLocation(), "astralis:adapt", 0.4f, 1f);
        player.sendActionBar(Component.text("§cADAPTAÇÃO COMPLETA: §f" + type));

        Bukkit.getScheduler().runTaskLater(Astralis.getInstance(), () -> {

            fullAdaptation.remove(id);
            damageLevels.remove(id);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 4));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 4));

        }, 20 * 20L);
    }

    private String getDamageType(EntityDamageEvent e) {
        return switch (e.getCause()) {
            case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> "Melee";
            case PROJECTILE -> "Projectile";
            case MAGIC, WITHER, DRAGON_BREATH -> "Magic";
            case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> "Explosion";
            case FIRE, FIRE_TICK, LAVA -> "Fire";
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