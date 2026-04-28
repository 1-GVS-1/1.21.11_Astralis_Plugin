package org.Thegvsss.Astralis.entity;

import org.bukkit.*;
import org.bukkit.entity.*;

public class Merchant {

    private static Villager merchant;

    public static void spawn(Player p) {

        Location loc = p.getLocation();

        merchant = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);

        merchant.setAI(false);
        merchant.setInvulnerable(true);
        merchant.setSilent(true);
        merchant.setCustomName("§5Astralis Merchant");
        merchant.setCustomNameVisible(true);

        merchant.addScoreboardTag("merchant");
        merchant.addScoreboardTag("server");

        p.sendMessage("§aMerchant spawnado!");
    }

    public static void remove(Player p) {

        if (merchant == null) return;

        merchant.remove();
        merchant = null;

        p.sendMessage("§cMerchant removido!");
    }
}