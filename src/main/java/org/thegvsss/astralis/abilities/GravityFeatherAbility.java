package org.thegvsss.astralis.abilities;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.thegvsss.astralis.utils.CooldownManager;
import org.thegvsss.astralis.utils.ItemUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GravityFeatherAbility implements Listener {

    private final JavaPlugin plugin;
    private final Set<UUID> flying = new HashSet<>();

    public GravityFeatherAbility(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!ItemUtils.hasTag(item, "gravity_feather")) return;
        if (CooldownManager.isOnCooldown(player, "gravity_feather")) return;

        e.setCancelled(true);

        UUID id = player.getUniqueId();

        // Toggle voo
        if (flying.contains(id)) {
            stopFlight(player);
            return;
        }

        flying.add(id);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.sendActionBar(Component.text("§b🪶 Voo ativado! §7(60s)"));
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 10, 0.3, 0.3, 0.3, 0.05);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 0.6f, 1.5f);

        // Countdown visual
        for (int sec = 55; sec >= 5; sec -= 10) {
            final int s = sec;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!flying.contains(id)) return;
                player.sendActionBar(Component.text("§b🪶 Voo: §f" + s + "s restantes"));
            }, (60 - sec) * 20L);
        }

        // Encerra após 60s
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (flying.contains(id)) stopFlight(player);
        }, 20 * 60L);

        CooldownManager.setCooldown(player, "gravity_feather", 120);
    }

    private void stopFlight(Player player) {
        flying.remove(player.getUniqueId());

        if (!player.isOnline()) return;

        // Só remove voo se não for criativo/espectador
        if (player.getGameMode() != org.bukkit.GameMode.CREATIVE
                && player.getGameMode() != org.bukkit.GameMode.SPECTATOR) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
        player.sendMessage("§c[Gravity Feather] §7Voo encerrado!");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_HURT, 0.5f, 1f);
    }
}
