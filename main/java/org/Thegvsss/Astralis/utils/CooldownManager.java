
package org.thegvsss.astralis.utils;

import java.util.*;
import org.bukkit.entity.Player;

public class CooldownManager {
    private static final Map<UUID, Long> map = new HashMap<>();

    public static boolean onCooldown(Player p){
        if(!map.containsKey(p.getUniqueId())) return false;
        return map.get(p.getUniqueId()) > System.currentTimeMillis();
    }

    public static void set(Player p, int sec){
        map.put(p.getUniqueId(), System.currentTimeMillis() + sec*1000L);
    }
}
