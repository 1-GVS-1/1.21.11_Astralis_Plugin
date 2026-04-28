package org.Thegvsss.Astralis.commands;

import org.Thegvsss.Astralis.entity.Merchant;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class AstralisCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        if (args.length == 0) {
            new org.Thegvsss.Astralis.gui.AstralisGui().open(p);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "merchant" -> Merchant.spawn(p);
            case "merchantkill" -> Merchant.remove(p);
        }

        return true;
    }
}