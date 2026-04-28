package org.thegvsss.astralis.abilities;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.thegvsss.astralis.utils.CooldownManager;
import org.thegvsss.astralis.utils.ItemUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Flamethrower — lança chamas na direção do olhar.
 * BUG CORRIGIDO: fuel era campo de instância (reseta a cada registro).
 * Agora usa Map<UUID, Integer> para fuel por player.
 * BUG CORRIGIDO: dano aplicado corretamente em qualquer LivingEntity (mobs + players).
 */
public class FlamethrowerAbility implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, Integer> fuel = new HashMap<>();

    public FlamethrowerAbility(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (!ItemUtils.hasTag(item, "flamethrower")) return;

        e.setCancelled(true);

        // Verificar cooldown de disparo contínuo (tick)
        if (CooldownManager.isOnCooldown(p, "flamethrower_tick")) return;

        UUID id = p.getUniqueId();
        int playerFuel = fuel.getOrDefault(id, 0);

        // Sem combustível → tentar recarregar
        if (playerFuel <= 0) {
            if (!p.getInventory().contains(Material.COAL)) {
                p.sendMessage("§cSem combustível! Precisa de §eCarvão§c.");
                return;
            }
            p.getInventory().removeItem(new ItemStack(Material.COAL, 1));
            fuel.put(id, 20 * 20); // 20 segundos de combustível
            p.sendMessage("§aCombustível carregado! §7(20s)");
        }

        // Consome fuel a cada disparo (tick de 2 ticks = 0.1s)
        fuel.put(id, playerFuel - 1);
        CooldownManager.setCooldown(p, "flamethrower_tick", 0); // sem delay extra

        // ─── Raio de chamas na direção do olhar ───────────────────────────
        Location eye = p.getEyeLocation();
        Vector dir = eye.getDirection().normalize();

        // Partículas e dano ao longo do raio (5 blocos)
        for (int i = 1; i <= 5; i++) {
            Location point = eye.clone().add(dir.clone().multiply(i));

            // Partículas de fogo
            p.getWorld().spawnParticle(Particle.FLAME, point, 3, 0.1, 0.1, 0.1, 0.01);
            if (i % 2 == 0) {
                p.getWorld().spawnParticle(Particle.SMOKE, point, 1, 0, 0, 0, 0.02);
            }

            // RayTrace para acertar entidades na linha
            RayTraceResult result = p.getWorld().rayTraceEntities(
                    eye.clone().add(dir.clone().multiply(i - 1)),
                    dir,
                    1.2,
                    1.0,
                    en -> en instanceof LivingEntity && !en.equals(p)
            );

            if (result != null && result.getHitEntity() instanceof LivingEntity le) {
                le.damage(1.5, p); // dano correto com fonte (não ignora armadura)
                le.setFireTicks(60); // 3 segundos de fogo
                // Empurra levemente na direção das chamas
                le.setVelocity(le.getVelocity().add(dir.clone().multiply(0.15)));
            }
        }

        // Som de fogo
        if (fuel.getOrDefault(id, 0) % 4 == 0) {
            p.getWorld().playSound(eye, Sound.BLOCK_FIRE_AMBIENT, 0.4f, 1.2f);
        }

        // Aviso de combustível baixo
        int remaining = fuel.getOrDefault(id, 0);
        if (remaining == 60) {
            p.sendActionBar(net.kyori.adventure.text.Component.text("§eCombustível acabando!"));
        } else if (remaining <= 0) {
            p.sendActionBar(net.kyori.adventure.text.Component.text("§cCombustível esgotado!"));
            fuel.remove(id);
        }
    }
}
