package org.thegvsss.astralis.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.thegvsss.astralis.AstralisCore;

import java.util.ArrayList;
import java.util.List;

public class MerchantRecipes {

    // 🪙 Criar Coin segura (com NBT)
    public static ItemStack createCoin() {
        ItemStack coin = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = coin.getItemMeta();

        meta.setDisplayName("§6Coin");

        NamespacedKey key = new NamespacedKey(AstralisCore.getInstance(), "coin");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        coin.setItemMeta(meta);
        return coin;
    }

    // 🧾 Aplicar trades no villager
    public static void applyTo(Villager villager) {

        List<MerchantRecipe> recipes = new ArrayList<>();

        // Exemplo: 5 coins = 1 diamond
        ItemStack coin = createCoin();
        coin.setAmount(5);

        ItemStack reward = new ItemStack(Material.DIAMOND);

        MerchantRecipe recipe = new MerchantRecipe(reward, 999);
        recipe.addIngredient(coin);

        recipes.add(recipe);

        villager.setRecipes(recipes);
    }
}