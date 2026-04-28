package org.Thegvsss.Astralis.abilities.client;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class RocketCrossbowAbility implements Listener {

    private final NamespacedKey key;

    public RocketCrossbowAbility(JavaPlugin plugin) {
        this.key = new NamespacedKey(plugin, "rocket_arrow");
    }

    // 🔹 Marca a flecha quando dispara
    @EventHandler
    public void onShoot(EntityShootBowEvent e) {

        if (!(e.getEntity() instanceof Player player)) return;

        ItemStack item = e.getBow();
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        if (!meta.getDisplayName().equals("§cRocket Crossbow")) return;

        if (e.getProjectile() instanceof AbstractArrow arrow) {
            PersistentDataContainer data = arrow.getPersistentDataContainer();
            data.set(key, PersistentDataType.INTEGER, 1);
        }
    }

    // 🔹 Executa no impacto
    @EventHandler
    public void onHit(ProjectileHitEvent e) {

        if (!(e.getEntity() instanceof AbstractArrow arrow)) return;

        PersistentDataContainer data = arrow.getPersistentDataContainer();
        if (!data.has(key, PersistentDataType.INTEGER)) return;

        Location loc = arrow.getLocation();
        if (loc.getWorld() == null) return;

        // Explosão controlada
        loc.getWorld().createExplosion(loc, 2f, false, false);

        for (Entity en : loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {

            if (en instanceof LivingEntity le) {
                le.damage(2.0);
                le.setVelocity(new Vector(0, 1.2, 0));
            }
        }

        arrow.remove(); // evita bug visual
    }
}