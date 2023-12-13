package org.murilonerdx.utils;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.murilonerdx.Hungergames;

import java.util.Collections;


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

    public static ItemStack createItemCustomCure() {
        ItemStack item = new ItemStack(Material.BREAD); // Tipo de item
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(
                Enchantment.DURABILITY, 5, true
        );
        meta.setDisplayName("This bread will bring you the cure you need");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createFireBoots() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();

        meta.setColor(Color.RED); // Definir a cor para vermelho
        meta.setDisplayName("Botas de Fogo");
        meta.addEnchant(Enchantment.PROTECTION_FIRE, 40, true);
        boots.setItemMeta(meta);

        return boots;
    }

    public static ItemStack createSpecialBow(String type) {
        ItemStack arrow = new ItemStack(Material.BOW, 1);
        ItemMeta meta = arrow.getItemMeta();

        meta.setDisplayName(type);
        meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&7Tipo: " + type)));
        arrow.setItemMeta(meta);

        return arrow;
    }

    public static ItemStack createSpecialArrow(String type, int amount) {
        ItemStack arrow = new ItemStack(Material.ARROW, amount);
        ItemMeta meta = arrow.getItemMeta();

        meta.setDisplayName(type);
        meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&7Tipo: " + type)));
        arrow.setItemMeta(meta);

        return arrow;
    }

    public static ItemStack createJumpBoots() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();

        meta.setColor(Color.WHITE); // Definir a cor para vermelho
        meta.setDisplayName("Botas do Ar");
        meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, 40, true);
        meta.addEnchant(Enchantment.PROTECTION_FALL, 200, true);
        boots.setItemMeta(meta);

        return boots;
    }

    public static ItemStack createwWaterIceBoots() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();

        meta.setColor(Color.BLUE); // Definir a cor para vermelho
        meta.setDisplayName("Botas de Gelo");
        meta.addEnchant(Enchantment.WATER_WORKER, 40, true);
        meta.addEnchant(Enchantment.LUCK, 40, true);
        boots.setItemMeta(meta);

        return boots;
    }

    public static ItemStack createItemCustomSword() {
        ItemStack item = new ItemStack(Material.WOODEN_SWORD); // Tipo de item
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(
                Enchantment.DURABILITY, 5, true
        );
        meta.addEnchant(
                Enchantment.KNOCKBACK, 5, true
        );
        meta.addEnchant(
                Enchantment.FIRE_ASPECT, 5, true
        );

        meta.addEnchant(
                Enchantment.DAMAGE_UNDEAD, 5, true
        );
        meta.setDisplayName("This sword is stronger than it looks");
        item.setItemMeta(meta);
        return item;
    }
}
