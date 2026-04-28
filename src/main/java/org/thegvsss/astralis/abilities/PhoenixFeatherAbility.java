package org.thegvsss.astralis.abilities;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.thegvsss.astralis.utils.CooldownManager;
import org.thegvsss.astralis.utils.ItemUtils;

/**
 * PhoenixFeather
 * Passiva: ao morrer com a pena no inventário, cancela a morte,
 * resurge no local com 50% de vida e Regeneração II por 5s.
 * Cooldown: 300s (5 minutos).
 */
public class PhoenixFeatherAbility implements Listener {

    private final JavaPlugin plugin;

    public PhoenixFeatherAbility(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();

        // Verifica se tem a pena em qualquer slot
        ItemStack feather = findFeather(player);
        if (feather == null) return;
        if (CooldownManager.isOnCooldown(player, "phoenix_feather")) return;

        // Cancela a morte
        e.setCancelled(true);

        // Restaura 50% de vida
        double maxHp = player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
        player.setHealth(maxHp * 0.5);
        player.setFoodLevel(15);
        player.setSaturation(5f);

        // Efeitos
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0, false, false));

        // FX de renascimento
        Location loc = player.getLocation();
        loc.getWorld().spawnParticle(Particle.FLAME, loc.add(0, 1, 0), 80, 0.8, 1, 0.8, 0.05);
        loc.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, loc, 40, 0.5, 1, 0.5, 0.2);
        loc.getWorld().playSound(loc, Sound.ITEM_TOTEM_USE, 1f, 0.7f);

        player.sendMessage("§6[Phoenix Feather] §fVocê renasceu das cinzas! Cooldown: §e5min");
        player.sendActionBar(Component.text("§6🔥 Phoenix: Renascido!"));

        CooldownManager.setCooldown(player, "phoenix_feather", 300);
    }

    private ItemStack findFeather(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (ItemUtils.hasTag(item, "phoenix_feather")) return item;
        }
        return null;
    }
}
