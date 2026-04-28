package org.thegvsss.astralis.economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.thegvsss.astralis.items.misc.Coin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Sistema de economia: moedas ganhas em quests simples (matar mobs).
 * Usadas na loja do merchant para buffs/efeitos permanentes e encantos além do máximo.
 */
public class EconomyManager implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, Integer> balances = new HashMap<>();
    private File dataFile;
    private FileConfiguration data;

    public EconomyManager(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    // ─── Quests simples: ganhar moedas matando mobs ────────────────────────

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player player)) return;

        int reward = switch (e.getEntity().getType()) {
            case ZOMBIE, SKELETON, SPIDER -> 1;
            case CREEPER, WITCH -> 2;
            case BLAZE, WITHER_SKELETON -> 3;
            case ENDERMAN -> 4;
            case ELDER_GUARDIAN, WARDEN -> 10;
            case ENDER_DRAGON, WITHER -> 25;
            default -> 0;
        };

        if (reward > 0) {
            addCoins(player, reward);
            if (reward >= 3) {
                player.sendActionBar(net.kyori.adventure.text.Component.text(
                        "§6+" + reward + " Coins §7(" + getBalance(player) + " total)")
                );
            }
        }
    }

    // ─── API de saldo ──────────────────────────────────────────────────────

    public int getBalance(Player p) {
        return balances.getOrDefault(p.getUniqueId(), 0);
    }

    public void addCoins(Player p, int amount) {
        UUID id = p.getUniqueId();
        balances.put(id, balances.getOrDefault(id, 0) + amount);
    }

    public boolean removeCoins(Player p, int amount) {
        int current = getBalance(p);
        if (current < amount) return false;
        balances.put(p.getUniqueId(), current - amount);
        return true;
    }

    public boolean hasCoins(Player p, int amount) {
        return getBalance(p) >= amount;
    }

    // ─── Persistência ──────────────────────────────────────────────────────

    private void load() {
        dataFile = new File(plugin.getDataFolder(), "economy.yml");
        if (!dataFile.exists()) {
            plugin.getDataFolder().mkdirs();
            try { dataFile.createNewFile(); } catch (IOException ex) { ex.printStackTrace(); }
        }
        data = YamlConfiguration.loadConfiguration(dataFile);
        if (data.contains("balances")) {
            for (String key : data.getConfigurationSection("balances").getKeys(false)) {
                balances.put(UUID.fromString(key), data.getInt("balances." + key));
            }
        }
    }

    public void saveAll() {
        balances.forEach((uuid, amount) -> data.set("balances." + uuid, amount));
        try { data.save(dataFile); } catch (IOException e) { e.printStackTrace(); }
    }
}
