package org.thegvsss.astralis.abilities;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.thegvsss.astralis.utils.CooldownManager;
import org.thegvsss.astralis.utils.ItemUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * ForbidenTotem
 * Passiva: ao receber dano fatal (HP cairia para 0), sobrevive com 0.5 de vida
 * e fica invencível por 10 segundos.
 * Cooldown: 60s após o fim da invencibilidade.
 * O item precisa estar no slot do elmo (HEAD).
 */
public class ForbidenTotemAbility implements Listener {

    private final JavaPlugin plugin;
    private final Set<UUID> invincible = new HashSet<>();

    public ForbidenTotemAbility(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        ItemStack helmet = player.getEquipment().getHelmet();
        if (!ItemUtils.hasTag(helmet, "forbiden_totem")) return;

        UUID id = player.getUniqueId();

        // Bloqueia dano enquanto invencível
        if (invincible.contains(id)) {
            e.setCancelled(true);
            player.sendActionBar(Component.text("§5🛡 Invencível!"));
            return;
        }

        // Verifica se o dano seria fatal
        double healthAfter = player.getHealth() - e.getFinalDamage();
        if (healthAfter > 0.5) return; // não é fatal

        // Cooldown do item
        if (CooldownManager.isOnCooldown(player, "forbiden_totem")) return;

        // Cancela o dano fatal
        e.setCancelled(true);

        // Seta HP mínimo
        player.setHealth(1.0);

        // Invencibilidade por 10s
        invincible.add(id);

        // Efeitos visuais
        player.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING,
                player.getLocation().add(0, 1, 0), 60, 0.5, 1, 0.5, 0.3);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 0.8f);

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1, false, false));
        player.sendMessage("§5[Forbiden Totem] §fInvencibilidade ativada por §e10s§f!");
        player.sendActionBar(Component.text("§5☠ Forbiden Totem: §fInvencível por 10s!"));

        // Encerra invencibilidade após 10s
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            invincible.remove(id);
            if (!player.isOnline()) return;
            // Efeito de exaustão pós-invencibilidade
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1, false, false));
            player.sendMessage("§c[Forbiden Totem] §7Invencibilidade encerrada. O preço foi cobrado.");
            player.sendActionBar(Component.text("§c☠ Totem esgotado."));
            CooldownManager.setCooldown(player, "forbiden_totem", 60);
        }, 20 * 10L);
    }
}
