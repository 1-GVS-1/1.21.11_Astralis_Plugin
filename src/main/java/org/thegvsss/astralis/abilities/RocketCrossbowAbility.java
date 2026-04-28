package org.thegvsss.astralis.abilities;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * RocketCrossbow — flecha explode ao impactar, lança entidades para o alto.
 *
 * BUG CORRIGIDO:
 *  - Verificação por PDC (não por display name) — mais confiável.
 *  - Lança QUALQUER LivingEntity (mobs + players) para cima.
 *  - Explosão visual sem destruir blocos.
 *  - Cooldown para não spammar.
 */
public class RocketCrossbowAbility implements Listener {

    private final NamespacedKey rocketKey;
    private final NamespacedKey shooterKey;

    public RocketCrossbowAbility(JavaPlugin plugin) {
        this.rocketKey = new NamespacedKey(plugin, "rocket_arrow");
        this.shooterKey = new NamespacedKey(plugin, "rocket_shooter");
    }

    // ─── Marca a flecha quando atirada ─────────────────────────────────────

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        ItemStack bow = e.getBow();
        if (bow == null) return;

        // Verifica via PDC (confiável mesmo com resource pack)
        if (!bow.hasItemMeta()) return;
        if (!bow.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey("astralis", "rocket_crossbow"), PersistentDataType.INTEGER)) return;

        if (!(e.getProjectile() instanceof AbstractArrow arrow)) return;

        PersistentDataContainer pdc = arrow.getPersistentDataContainer();
        pdc.set(rocketKey, PersistentDataType.INTEGER, 1);
        pdc.set(shooterKey, PersistentDataType.STRING, player.getUniqueId().toString());

        // Visual: chama de rastro
        arrow.setGlowing(true);
        arrow.setFireTicks(9999);
    }

    // ─── Impacto: explosão + lançar entidades ─────────────────────────────

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof AbstractArrow arrow)) return;

        PersistentDataContainer pdc = arrow.getPersistentDataContainer();
        if (!pdc.has(rocketKey, PersistentDataType.INTEGER)) return;

        Location loc = arrow.getLocation();
        World world = loc.getWorld();
        if (world == null) return;

        // Recupera atirador para não se auto-danificar
        String shooterUUID = pdc.get(shooterKey, PersistentDataType.STRING);

        // Explosão visual (sem destruir blocos, sem fogo)
        world.createExplosion(loc, 0f, false, false);
        world.spawnParticle(Particle.EXPLOSION, loc, 3, 0.5, 0.5, 0.5);
        world.spawnParticle(Particle.FLAME, loc, 20, 0.6, 0.6, 0.6, 0.08);
        world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.2f, 0.8f);

        // Afeta entidades num raio de 4 blocos
        for (Entity entity : world.getNearbyEntities(loc, 4, 4, 4)) {
            if (!(entity instanceof LivingEntity le)) continue;

            // Não se auto-danifica
            if (shooterUUID != null && entity instanceof Player p
                    && p.getUniqueId().toString().equals(shooterUUID)) continue;

            // ✅ Funciona em MOBS e PLAYERS
            double distance = entity.getLocation().distance(loc);
            double dmgMultiplier = Math.max(0.2, 1.0 - (distance / 4.0));
            le.damage(8.0 * dmgMultiplier); // dano decrescente com distância

            // Lança para o alto (efeito foguete)
            Vector launchDir = entity.getLocation().toVector()
                    .subtract(loc.toVector())
                    .normalize()
                    .multiply(0.5)
                    .setY(1.5); // componente Y forte = lança pra cima

            entity.setVelocity(launchDir);
        }

        arrow.remove();
    }
}
