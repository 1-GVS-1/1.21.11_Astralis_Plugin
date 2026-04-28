package org.Thegvsss.Astralis.events.global;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EffectsRelics {

    public static void playAdaptEffects(Player player) {

        player.getWorld().spawnParticle(
                Particle.ENCHANT,
                player.getLocation().add(0, 1, 0),
                40,
                0.6, 1.2, 0.6,
                0.1
        );

        player.getWorld().spawnParticle(
                Particle.ELECTRIC_SPARK,
                player.getLocation(),
                25,
                0.5, 1, 0.5,
                0.2
        );

        player.getWorld().spawnParticle(
                Particle.CLOUD,
                player.getLocation(),
                15,
                0.4, 0.8, 0.4,
                0.02
        );

        player.getWorld().playSound(player.getLocation(), "astralis:adapt", 0.4f, 1.2f);
    }
}