package org.thegvsss.astralis.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * CooldownManager unificado - suporta múltiplas habilidades por player.
 */
public class CooldownManager {

    // skill -> (UUID -> expirationTime)
    private static final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

    public static boolean isOnCooldown(Player p, String skill) {
        Map<UUID, Long> map = cooldowns.get(skill);
        if (map == null) return false;
        Long exp = map.get(p.getUniqueId());
        if (exp == null) return false;
        if (System.currentTimeMillis() >= exp) {
            map.remove(p.getUniqueId());
            return false;
        }
        return true;
    }

    public static void setCooldown(Player p, String skill, int seconds) {
        cooldowns.computeIfAbsent(skill, k -> new HashMap<>())
                .put(p.getUniqueId(), System.currentTimeMillis() + seconds * 1000L);
    }

    public static long getRemainingSeconds(Player p, String skill) {
        Map<UUID, Long> map = cooldowns.get(skill);
        if (map == null) return 0;
        Long exp = map.get(p.getUniqueId());
        if (exp == null) return 0;
        long remaining = (exp - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }

    // Compat legado (sem skill específico)
    public static boolean onCooldown(Player p) { return isOnCooldown(p, "default"); }
    public static void set(Player p, int sec) { setCooldown(p, "default", sec); }
}
