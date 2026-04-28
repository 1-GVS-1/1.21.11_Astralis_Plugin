package org.thegvsss.astralis;

import org.bukkit.plugin.java.JavaPlugin;
import org.thegvsss.astralis.abilities.*;
import org.thegvsss.astralis.commands.AstralisCommand;
import org.thegvsss.astralis.economy.EconomyManager;
import org.thegvsss.astralis.events.global.EffectsRelics;
import org.thegvsss.astralis.gui.GuiListener;
import org.thegvsss.astralis.items.weapons.BlastTrident;
import org.thegvsss.astralis.listener.*;
import org.thegvsss.astralis.quest.QuestManager;
import org.thegvsss.astralis.shop.MerchantShopListener;

public class AstralisCore extends JavaPlugin {

    private static AstralisCore instance;
    private EconomyManager economyManager;
    private QuestManager questManager;

    @Override
    public void onEnable() {
        instance = this;

        // Managers
        economyManager = new EconomyManager(this);
        questManager = new QuestManager(this);

        // Comando principal
        if (getCommand("astralis") != null) {
            getCommand("astralis").setExecutor(new AstralisCommand(this));
        } else {
            getLogger().severe("Comando 'astralis' não encontrado no plugin.yml!");
        }

        // GUI
        getServer().getPluginManager().registerEvents(new GuiListener(), this);

        // Listeners de sistema
        getServer().getPluginManager().registerEvents(new MerchantShopListener(this), this);
        getServer().getPluginManager().registerEvents(new MerchantListener(), this);
        getServer().getPluginManager().registerEvents(new ResourcePackListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(), this); // CORRIGIDO
        getServer().getPluginManager().registerEvents(new NoLostYourItems(), this);

        // Abilities originais
        getServer().getPluginManager().registerEvents(new DharmaAbility(this), this);
        getServer().getPluginManager().registerEvents(new GravityFeatherAbility(this), this);
        getServer().getPluginManager().registerEvents(new EnderSwordAbility(), this);
        getServer().getPluginManager().registerEvents(new FlamethrowerAbility(this), this);
        getServer().getPluginManager().registerEvents(new SwordOfCrushingAbility(this), this);

        // Abilities novas
        getServer().getPluginManager().registerEvents(new DragonSwordAbility(), this);
        getServer().getPluginManager().registerEvents(new HellSwordAbility(this), this);
        getServer().getPluginManager().registerEvents(new ForbidenTotemAbility(this), this);
        getServer().getPluginManager().registerEvents(new PhoenixFeatherAbility(this), this);
        getServer().getPluginManager().registerEvents(new TimeShardAbility(this), this);
        getServer().getPluginManager().registerEvents(new BlastTridentAbility(this), this);

        // Quests & Economia
        getServer().getPluginManager().registerEvents(questManager, this);
        getServer().getPluginManager().registerEvents(economyManager, this);

        getLogger().info("§aAstralisFull v2.1 ativado com sucesso!");
    }

    @Override
    public void onDisable() {
        if (economyManager != null) economyManager.saveAll();
        if (questManager != null) questManager.saveAll();
        getLogger().info("§cAstralisFull desativado.");
    }

    public static AstralisCore getInstance() { return instance; }
    public EconomyManager getEconomyManager() { return economyManager; }
    public QuestManager getQuestManager() { return questManager; }
}
