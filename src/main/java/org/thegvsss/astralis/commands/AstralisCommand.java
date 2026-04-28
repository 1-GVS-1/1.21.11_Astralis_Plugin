package org.thegvsss.astralis.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.thegvsss.astralis.AstralisCore;
import org.thegvsss.astralis.entity.Merchant;
import org.thegvsss.astralis.gui.AstralisGui;
import org.thegvsss.astralis.items.relics.ForbidenTotem;
import org.thegvsss.astralis.items.relics.PhoenixFeather;
import org.thegvsss.astralis.items.relics.TimeShard;
import org.thegvsss.astralis.items.weapons.DragonSword;
import org.thegvsss.astralis.items.weapons.HellSword;
import org.thegvsss.astralis.shop.MerchantShopListener;

public class AstralisCommand implements CommandExecutor {

    private final AstralisCore plugin;

    public AstralisCommand(AstralisCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cSó jogadores podem usar este comando.");
            return true;
        }

        if (args.length == 0) {
            new AstralisGui(plugin).open(p);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "merchant"     -> Merchant.spawn(p);
            case "merchantkill" -> Merchant.remove(p);
            case "shop"         -> p.openInventory(MerchantShopListener.create());
            case "quest"        -> plugin.getQuestManager().showProgress(p);
            case "coins"        -> p.sendMessage("§6Seus Coins: §f" +
                    plugin.getEconomyManager().getBalance(p));

            case "addcoins" -> {
                if (!p.hasPermission("astralis.admin")) break;
                if (args.length < 3) { p.sendMessage("§cUso: /astralis addcoins <player> <amount>"); break; }
                Player target = org.bukkit.Bukkit.getPlayerExact(args[1]);
                if (target == null) { p.sendMessage("§cJogador não encontrado."); break; }
                int amount = Integer.parseInt(args[2]);
                plugin.getEconomyManager().addCoins(target, amount);
                p.sendMessage("§a+" + amount + " coins dados para " + target.getName());
                target.sendMessage("§a+" + amount + " coins recebidos de " + p.getName());
            }

            // /astralis give <item> [player]
            case "give" -> {
                if (!p.hasPermission("astralis.admin")) {
                    p.sendMessage("§cSem permissão."); break;
                }
                if (args.length < 2) {
                    p.sendMessage("§cUso: /astralis give <item> [player]");
                    p.sendMessage("§7Itens: dragon_sword, hell_sword, forbiden_totem, phoenix_feather, time_shard");
                    break;
                }
                Player target = args.length >= 3
                        ? org.bukkit.Bukkit.getPlayerExact(args[2])
                        : p;
                if (target == null) { p.sendMessage("§cJogador não encontrado."); break; }

                var item = switch (args[1].toLowerCase()) {
                    case "dragon_sword"    -> DragonSword.create();
                    case "hell_sword"      -> HellSword.create();
                    case "forbiden_totem"  -> ForbidenTotem.create();
                    case "phoenix_feather" -> PhoenixFeather.create();
                    case "time_shard"      -> TimeShard.create();
                    default -> null;
                };

                if (item == null) {
                    p.sendMessage("§cItem desconhecido. Use: dragon_sword, hell_sword, forbiden_totem, phoenix_feather, time_shard");
                } else {
                    target.getInventory().addItem(item);
                    p.sendMessage("§aItem §f" + args[1] + " §adado para §f" + target.getName());
                    if (!target.equals(p))
                        target.sendMessage("§aVocê recebeu §f" + args[1] + " §ade §f" + p.getName());
                }
            }

            case "help" -> {
                p.sendMessage("§6=== Astralis Commands ===");
                p.sendMessage("§e/astralis §7— Abre o menu");
                p.sendMessage("§e/astralis shop §7— Abre a loja");
                p.sendMessage("§e/astralis quest §7— Ver progresso das quests");
                p.sendMessage("§e/astralis coins §7— Ver seu saldo");
                p.sendMessage("§e/astralis merchant §7— Spawna o mercador (OP)");
                p.sendMessage("§e/astralis give <item> [player] §7— Dar item (OP)");
            }

            default -> p.sendMessage("§cComando inválido. Use §e/astralis help");
        }

        return true;
    }
}
