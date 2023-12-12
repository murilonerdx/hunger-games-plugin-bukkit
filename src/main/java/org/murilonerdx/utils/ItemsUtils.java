package org.murilonerdx.utils;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.murilonerdx.Hungergames;


public class ItemsUtils {
    // Criar o item personalizado
    public static ItemStack createLightningProtectionItem() {
        ItemStack item = new ItemStack(Material.IRON_HELMET); // Tipo de item
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
        meta.addEnchant(Enchantment.WATER_WORKER, 100, true);
        meta.setDisplayName("Lightning Protection Item");
        item.setItemMeta(meta);
        return item;
    }

    // Criar o item personalizado
    public static ItemStack createSpeedInvisibilityItem() {
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE); // Tipo de item
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Speed and Invisibility Item");
        item.setItemMeta(meta);
        return item;
    }

    public static void recipeCustomCraft() {
        // Cria uma nova receita
        ItemStack result = createLightningProtectionItem();// Item temporário, será substituído depois
        NamespacedKey key = new NamespacedKey(Hungergames.instance, "custom_crafting");
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        // Define o padrão da receita (exemplo: 3x3 com pedras e gravetos)
        recipe.shape("SSS", "SWS", "SSS");
        recipe.setIngredient('S', Material.STONE);
        recipe.setIngredient('W', Material.DIAMOND);

        // Registra a receita
        Bukkit.addRecipe(recipe);
    }

    public static boolean isArmor(Material material) {
        return material.name().endsWith("_HELMET") ||
                material.name().endsWith("_CHESTPLATE") ||
                material.name().endsWith("_LEGGINGS") ||
                material.name().endsWith("_BOOTS");
    }

    public static boolean isWeapon(Material material) {
        return material.name().endsWith("_SWORD") ||
                material.name().endsWith("_AXE") ||
                material.name().endsWith("_BOW") ||
                material.name().endsWith("_CROSSBOW");
    }

    public static boolean selectItems(Material material) {
        return isArmor(material) || isWeapon(material) ;
    }
}
