package org.murilonerdx.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Objects;

import static org.murilonerdx.utils.PlayerUtils.giveCursedItem;
import static org.murilonerdx.utils.PlayerUtils.strikePlayerWithLightning;

public class CursedItemListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().contains(Material.DIAMOND_SWORD)) {
            // Aplicar efeitos negativos
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1)); // Exemplo: Lentidão
        }
    }

    private boolean isCursedItem(ItemStack item) {
        return item != null && item.hasItemMeta() && Objects.requireNonNull(item.getItemMeta()).hasLore() && Objects.requireNonNull(item.getItemMeta().getLore()).contains("Este item está amaldiçoado");
    }

    @EventHandler
    public void onModifyCursedItem(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if (isCursedItem(clickedItem)) {
            // Verifique se o clique é em uma bigorna ou em uma mesa de trabalho
            if (event.getInventory().getType() == InventoryType.ANVIL || event.getInventory().getType() == InventoryType.WORKBENCH) {
                event.setCancelled(true);
                // Você pode enviar uma mensagem ao jogador para informar que o item não pode ser usado
                if (event.getWhoClicked() instanceof Player) {
                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage(ChatColor.RED + "Você está amaldiçoado");
                    player.sendMessage(ChatColor.YELLOW + "Você SERÁ PUNIDO!");
                    strikePlayerWithLightning(player.getLocation());
                    giveCursedItem(player);
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore.contains("Este item está amaldiçoado")) {
                event.setCancelled(true); // Cancela a ação de jogar o item fora
            }
        }
    }

    @EventHandler
    public void onPlayerDrinkMilk(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.MILK_BUCKET) {
            Player player = event.getPlayer();
            player.getInventory().remove(Material.DIAMOND_SWORD); // Remove o item amaldiçoado
        }
    }
}
