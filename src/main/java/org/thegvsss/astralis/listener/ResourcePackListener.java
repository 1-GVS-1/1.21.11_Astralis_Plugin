package org.thegvsss.astralis.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;

public class ResourcePackListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setResourcePack(
                "https://download.mc-packs.net/pack/b71d79370632277c3cb993ef15b2012203924784.zip"
        );
    }
}
