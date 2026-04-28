package org.thegvsss.astralis.listener;

import org.bukkit.attribute.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.thegvsss.astralis.utils.ItemUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * AnvilListener — corrigido.
 *
 * BUG ORIGINAL:
 *   PrepareAnvilEvent define o resultado, mas quando o jogador clica para pegar,
 *   o Minecraft verifica XP e reseta o inventário antes de entregar o item,
 *   fazendo o item voltar para o slot 0.
 *
 * CORREÇÃO:
 *   1. PrepareAnvilEvent: monta o item resultante e armazena no cache.
 *   2. InventoryClickEvent: intercepta o clique no slot de resultado da bigorna,
 *      entrega o item cacheado manualmente e limpa os slots de entrada.
 *      Assim o XP nunca é cobrado e o item não volta para a bigorna.
 */
public class AnvilListener implements Listener {

    // Cache: identityHashCode do AnvilInventory -> ItemStack resultante
    private final Map<Integer, ItemStack> pendingResult = new HashMap<>();

    // ─── Etapa 1: preparar resultado ─────────────────────────────────────────

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        ItemStack base = e.getInventory().getItem(0);
        ItemStack book = e.getInventory().getItem(1);

        if (base == null || book == null) {
            pendingResult.remove(System.identityHashCode(e.getInventory()));
            return;
        }

        ItemStack result = base.clone();
        boolean changed = false;

        // UNBREAKABLE
        if (ItemUtils.hasTag(book, "book_unbreakable")) {
            var meta = result.getItemMeta();
            meta.setUnbreakable(true);
            result.setItemMeta(meta);
            changed = true;
        }

        // +30% DANO
        if (ItemUtils.hasTag(book, "book_damage")) {
            var meta = result.getItemMeta();
            meta.addAttributeModifier(
                    Attribute.ATTACK_DAMAGE,
                    new AttributeModifier(
                            UUID.randomUUID(), "astralis_dmg_boost",
                            0.30,
                            AttributeModifier.Operation.MULTIPLY_SCALAR_1
                    )
            );
            result.setItemMeta(meta);
            changed = true;
        }

        // +1 em TODOS os encantos (além do máximo via unsafe)
        if (ItemUtils.hasTag(book, "book_enchant")) {
            result.getEnchantments().forEach((ench, lvl) ->
                    result.addUnsafeEnchantment(ench, lvl + 1)
            );
            changed = true;
        }

        if (changed) {
            pendingResult.put(System.identityHashCode(e.getInventory()), result);
            e.setResult(result);
            // Custo 0 para não acionar a verificação de XP do Minecraft
            e.getInventory().setRepairCost(0);
        } else {
            pendingResult.remove(System.identityHashCode(e.getInventory()));
        }
    }

    // ─── Etapa 2: entregar item ao clicar no slot de resultado ───────────────

    @EventHandler(priority = EventPriority.HIGH)
    public void onAnvilClick(InventoryClickEvent e) {
        if (!(e.getInventory() instanceof AnvilInventory anvilInv)) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;
        if (e.getCurrentItem() == null) return;
        if (!(e.getWhoClicked() instanceof org.bukkit.entity.Player player)) return;

        int key = System.identityHashCode(anvilInv);
        ItemStack cached = pendingResult.get(key);
        if (cached == null) return; // não é modificação nossa — Minecraft trata normalmente

        // Cancela o comportamento vanilla (que cobrava XP e resetava os slots)
        e.setCancelled(true);

        // Entrega o item ao jogador
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(cached.clone());
        if (!leftover.isEmpty()) {
            // Inventário cheio: dropa no chão
            leftover.values().forEach(drop ->
                    player.getWorld().dropItem(player.getLocation(), drop)
            );
        }

        // Remove os itens dos slots de entrada
        anvilInv.setItem(0, null);
        anvilInv.setItem(1, null);

        // Limpa cache
        pendingResult.remove(key);

        player.sendActionBar(net.kyori.adventure.text.Component.text("§aItem melhorado com sucesso!"));
        player.getWorld().playSound(player.getLocation(),
                org.bukkit.Sound.BLOCK_ANVIL_USE, 1f, 1.2f);
    }
}
