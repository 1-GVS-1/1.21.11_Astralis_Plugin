package org.Thegvsss.Astralis.listener;
import org.Thegvsss.Astralis.gui.AstralisGui;
import org.Thegvsss.Astralis.utils.gui.WeaponsGui;
import org.Thegvsss.Astralis.utils.gui.RelicsGui;
import org.Thegvsss.Astralis.utils.gui.MiscGui;
import org.Thegvsss.Astralis.gui.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Só cancela se for a GUI de cima
        if (!(event.getView().getTopInventory().getHolder() instanceof Object holder)) return;

        if (event.getClickedInventory() == null) return;

        // Se clicou no inventário de baixo (player), NÃO cancela
        if (event.getClickedInventory().equals(player.getInventory())) return;

        // Aqui sim cancela (GUI)
        event.setCancelled(true);

        int slot = event.getSlot();

        if (holder instanceof AstralisGui gui) {
            gui.handle(player, slot);
        }

        if (holder instanceof WeaponsGui gui) {
            gui.handle(player, slot);
        }

        if (holder instanceof RelicsGui gui) {
            gui.handle(player, slot);
        }

        if (holder instanceof MiscGui gui) {
            gui.handle(player, slot);
        }
    }
}