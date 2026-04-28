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
 * HellSword
 * Ativa (clique direito): cria barreira de fogo ao redor do jogador.
 * Inimigos no raio de 4 blocos ficam em chamas e recebem dano.
 * O próprio jogador fica temporariamente imune ao fogo.
 */
public class HellSwordAbility implements Listener {

    private final JavaPlugin plugin;

    public HellSwordAbility(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!ItemUtils.hasTag(item, "hell_sword")) return;
        if (CooldownManager.isOnCooldown(player, "hell_sword")) return;

        e.setCancelled(true);

        // Imunidade ao fogo para o jogador
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, false, false));

        // FX visual de barreira
        Location loc = player.getLocation();
        double radius = 4.0;
        for (double angle = 0; angle < Math.PI * 2; angle += Math.PI / 16) {
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            loc.getWorld().spawnParticle(Particle.FLAME,
                    loc.clone().add(x, 0.5, z), 3, 0.1, 0.3, 0.1, 0.02);
            loc.getWorld().spawnParticle(Particle.LAVA,
                    loc.clone().add(x, 0.1, z), 1, 0, 0, 0, 0);
        }
        loc.getWorld().playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1f, 0.8f);
        loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 0.6f, 0.5f);

        // Aplica fogo nos inimigos próximos
        boolean hit = false;
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof LivingEntity le)) continue;
            if (entity.equals(player)) continue;

            le.setFireTicks(100); // 5s de fogo
            le.damage(4.0, player);
            le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 1, false, false));

            entity.getWorld().spawnParticle(Particle.FLAME,
                    entity.getLocation().add(0, 1, 0), 15, 0.3, 0.5, 0.3, 0.05);
            hit = true;
        }

        if (!hit) {
            player.sendMessage("§c[Hell Sword] §7Nenhum inimigo na barreira.");
        }

        player.sendActionBar(Component.text("§c🔥 Barreira do Inferno! Cooldown: §f60s"));
        CooldownManager.setCooldown(player, "hell_sword", 60);
    }
}
