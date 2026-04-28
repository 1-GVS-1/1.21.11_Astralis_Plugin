package org.Thegvsss.Astralis;

import org.Thegvsss.Astralis.abilities.client.DharmaAbility;
import org.Thegvsss.Astralis.abilities.client.FlamethrowerAbility;
import org.Thegvsss.Astralis.abilities.client.GravityFeatherAbility;
import org.Thegvsss.Astralis.abilities.client.RocketCrossbowAbility;
import org.Thegvsss.Astralis.abilities.weapons.EnderSwordAbility;
import org.Thegvsss.Astralis.abilities.weapons.SwordOfCrusingAbility;
import org.Thegvsss.Astralis.commands.AstralisCommand;
import org.Thegvsss.Astralis.listener.*;
import org.Thegvsss.Astralis.shop.MerchantShopListener;
import org.Thegvsss.Astralis.utils.listener.AnvilListener;
import org.Thegvsss.Astralis.utils.listener.NoLostYourItems;
import org.bukkit.plugin.java.JavaPlugin;

public class Astralis extends JavaPlugin {

    private static Astralis instance;

    @Override
    public void onEnable() {
        instance = this;

        // Comando seguro
        if (getCommand("astralis") != null) {
            getCommand("astralis").setExecutor(new AstralisCommand());
        } else {
            getLogger().severe("Comando 'astralis' não encontrado no plugin.yml!");
        }

        // Listeners principais
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new MerchantShopListener(), this);
        getServer().getPluginManager().registerEvents(new MerchantListener(), this);
        getServer().getPluginManager().registerEvents(new ResourcePackListener(), this);

        // Abilities
        getServer().getPluginManager().registerEvents(new GravityFeatherAbility(), this);
        getServer().getPluginManager().registerEvents(new DharmaAbility(), this);
        getServer().getPluginManager().registerEvents(new EnderSwordAbility(), this);
        getServer().getPluginManager().registerEvents(new FlamethrowerAbility(), this);
        getServer().getPluginManager().registerEvents(new RocketCrossbowAbility(this), this);
        getServer().getPluginManager().registerEvents(new SwordOfCrusingAbility(), this);

        // Utils
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getServer().getPluginManager().registerEvents(new NoLostYourItems(), this);
    }

    public static Astralis getInstance() {
        return instance;
    }
}