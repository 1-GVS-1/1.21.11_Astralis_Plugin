
package org.thegvsss.astralis;

import org.bukkit.plugin.java.JavaPlugin;
import org.thegvsss.astralis.events.*;

public class AstralisCore extends JavaPlugin {

    private static AstralisCore instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new SwordListener(), this);
        getServer().getPluginManager().registerEvents(new RelicListener(), this);

        getLogger().info("Astralis FULL enabled!");
    }

    public static AstralisCore getInstance() {
        return instance;
    }
}
