package org.thegvsss.astralis.quest;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.thegvsss.astralis.items.relics.Dharma;
import org.thegvsss.astralis.items.relics.GravityFeather;
import org.thegvsss.astralis.utils.ItemUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Sistema de Quests para Relíquias.
 *
 * LORE: Cada relíquia é ÚNICA no mundo. As quests são para obtê-las.
 * Uma vez que um player conquista a relíquia, a quest fecha para todos
 * (a relíquia foi "descoberta") — mas a lore permite que ela seja
 * possivelmente duplicada via evento especial no futuro.
 *
 * Quests disponíveis:
 *  - DHARMA: Matar 50 mobs de fogo (Blaze, Magma Cube) + 1 Wither
 *  - GRAVITY_FEATHER: Matar 100 Enderman + 1 Ender Dragon
 */
public class QuestManager implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, Map<String, Integer>> progress = new HashMap<>();
    private final Set<String> completedRelics = new HashSet<>(); // relíquias já conquistadas
    private File dataFile;
    private FileConfiguration data;

    // Objetivos das quests
    public static final Map<String, Map<String, Integer>> QUEST_GOALS = new LinkedHashMap<>();

    static {
        // Quest Dharma
        Map<String, Integer> dharmaGoals = new LinkedHashMap<>();
        dharmaGoals.put("BLAZE", 30);
        dharmaGoals.put("MAGMA_CUBE", 20);
        dharmaGoals.put("WITHER", 1);
        QUEST_GOALS.put("DHARMA", dharmaGoals);

        // Quest Gravity Feather
        Map<String, Integer> featherGoals = new LinkedHashMap<>();
        featherGoals.put("ENDERMAN", 100);
        featherGoals.put("ENDER_DRAGON", 1);
        QUEST_GOALS.put("GRAVITY_FEATHER", featherGoals);
    }

    public QuestManager(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    // ─── Evento: matar mob ─────────────────────────────────────────────────

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player player)) return;

        String mobKey = e.getEntityType().name();
        UUID id = player.getUniqueId();

        for (Map.Entry<String, Map<String, Integer>> questEntry : QUEST_GOALS.entrySet()) {
            String questId = questEntry.getKey();

            // Já foi conquistada por alguém?
            if (completedRelics.contains(questId)) continue;

            // Esse mob é objetivo dessa quest?
            if (!questEntry.getValue().containsKey(mobKey)) continue;

            // Incrementa progresso
            Map<String, Integer> playerProgress = progress.computeIfAbsent(id, k -> new HashMap<>());
            String progressKey = questId + ":" + mobKey;
            int current = playerProgress.getOrDefault(progressKey, 0) + 1;
            int goal = questEntry.getValue().get(mobKey);
            playerProgress.put(progressKey, Math.min(current, goal));

            player.sendActionBar(Component.text(
                    "§6Quest §e" + formatQuestName(questId) + " §7— §f" + mobKey + ": §a" + Math.min(current, goal) + "§7/§f" + goal
            ));

            // Checa se a quest toda foi completada
            if (isQuestComplete(id, questId)) {
                completeQuest(player, questId);
            }
        }
    }

    // ─── Verificação de completude ─────────────────────────────────────────

    private boolean isQuestComplete(UUID id, String questId) {
        Map<String, Integer> playerProgress = progress.getOrDefault(id, new HashMap<>());
        Map<String, Integer> goals = QUEST_GOALS.get(questId);
        if (goals == null) return false;

        for (Map.Entry<String, Integer> goal : goals.entrySet()) {
            String progressKey = questId + ":" + goal.getKey();
            int current = playerProgress.getOrDefault(progressKey, 0);
            if (current < goal.getValue()) return false;
        }
        return true;
    }

    // ─── Completar quest e dar relíquia ────────────────────────────────────

    private void completeQuest(Player player, String questId) {
        completedRelics.add(questId);

        ItemStack relic = switch (questId) {
            case "DHARMA" -> Dharma.create();
            case "GRAVITY_FEATHER" -> GravityFeather.create();
            default -> null;
        };

        if (relic == null) return;

        player.getInventory().addItem(relic);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);

        // Anúncio global
        String relicName = formatQuestName(questId);
        Bukkit.broadcast(Component.text(
                "§6§l★ §e" + player.getName() + " §6conquistou a relíquia §f" + relicName + "§6! §7(Única no mundo)"
        ));

        // Mensagem de lore
        player.sendMessage("");
        player.sendMessage("§6§lRELÍQUIA OBTIDA: §f" + relicName);
        player.sendMessage("§7Esta é a única §f" + relicName + " §7que existirá...");
        player.sendMessage("§7§o(Ou será?  A duplicação ainda é um mistério na lore.)");
        player.sendMessage("");

        saveAll();
    }

    // ─── Comando: ver progresso ────────────────────────────────────────────

    public void showProgress(Player player) {
        player.sendMessage("§6§l=== Suas Quests ===");
        UUID id = player.getUniqueId();
        Map<String, Integer> playerProgress = progress.getOrDefault(id, new HashMap<>());

        for (Map.Entry<String, Map<String, Integer>> questEntry : QUEST_GOALS.entrySet()) {
            String questId = questEntry.getKey();
            String name = formatQuestName(questId);

            if (completedRelics.contains(questId)) {
                player.sendMessage("§a✔ " + name + " §7— §fRelíquia já conquistada no mundo");
                continue;
            }

            player.sendMessage("§e⚔ Quest: §f" + name);
            for (Map.Entry<String, Integer> goal : questEntry.getValue().entrySet()) {
                String progressKey = questId + ":" + goal.getKey();
                int current = playerProgress.getOrDefault(progressKey, 0);
                String bar = progressBar(current, goal.getValue());
                player.sendMessage("  §7" + goal.getKey() + ": " + bar + " §f" + current + "§7/§f" + goal.getValue());
            }
        }
    }

    private String progressBar(int current, int max) {
        int filled = (int) ((current / (double) max) * 10);
        StringBuilder sb = new StringBuilder("§a");
        for (int i = 0; i < 10; i++) {
            if (i == filled) sb.append("§7");
            sb.append("█");
        }
        return sb.toString();
    }

    private String formatQuestName(String id) {
        return switch (id) {
            case "DHARMA" -> "Dharma";
            case "GRAVITY_FEATHER" -> "Gravity Feather";
            default -> id;
        };
    }

    // ─── Persistência ──────────────────────────────────────────────────────

    private void load() {
        dataFile = new File(plugin.getDataFolder(), "quests.yml");
        if (!dataFile.exists()) {
            plugin.getDataFolder().mkdirs();
            try { dataFile.createNewFile(); } catch (IOException ex) { ex.printStackTrace(); }
        }
        data = YamlConfiguration.loadConfiguration(dataFile);

        // Carrega relíquias completadas
        List<String> completed = data.getStringList("completedRelics");
        completedRelics.addAll(completed);

        // Carrega progresso
        if (data.contains("progress")) {
            for (String uuidStr : data.getConfigurationSection("progress").getKeys(false)) {
                UUID id = UUID.fromString(uuidStr);
                Map<String, Integer> map = new HashMap<>();
                for (String key : data.getConfigurationSection("progress." + uuidStr).getKeys(false)) {
                    map.put(key, data.getInt("progress." + uuidStr + "." + key));
                }
                progress.put(id, map);
            }
        }
    }

    public void saveAll() {
        data.set("completedRelics", new ArrayList<>(completedRelics));
        progress.forEach((uuid, map) -> {
            map.forEach((key, val) -> data.set("progress." + uuid + "." + key, val));
        });
        try { data.save(dataFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public Set<String> getCompletedRelics() { return completedRelics; }
}
