package org.murilonerdx.scheduler;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.murilonerdx.Hungergames;
import org.murilonerdx.utils.EventsUtil;

import java.util.*;

import static org.bukkit.Bukkit.getServer;
import static org.murilonerdx.Hungergames.*;
import static org.murilonerdx.utils.ItemsUtils.*;
import static org.murilonerdx.utils.PlayerUtils.*;

public class BootsEffectTask extends BukkitRunnable {
    private Map<Location, Material> changedBlocks = new HashMap<>();
    private Player player;
    private ItemStack boots;

    public BootsEffectTask(Player player, ItemStack boots) {
        this.player = player;
        this.boots = boots;
    }

    @Override
    public void run() {
        // Verifica se o jogador ainda está online e usando as botas
        if (player.isOnline() && player.getInventory().getBoots() != null && player.getInventory().getBoots().isSimilar(boots)) {
            // Aplica os efeitos
            applyBootsEffects(player, boots);
        } else {
            // Cancela a tarefa se as botas não estiverem sendo usadas
            this.cancel();
        }
    }

    private void applyBootsEffects(Player player, ItemStack boots) {
        if (boots.isSimilar(createFireBoots())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 1, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 1, true, true));
            // Adicione outros efeitos conforme necessário
            player.getLocation().getBlock().setType(Material.FIRE);
        } else if (boots.isSimilar(createwWaterIceBoots())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 3, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200, 100, false, false));
            // Adicione outros efeitos conforme necessário
            transformBlocksAroundPlayer(player);
        } else if (boots.isSimilar(createJumpBoots())) {
            player.playEffect(EntityEffect.ARROW_PARTICLES);
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 20, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2, false, false));
            // Adicione outros efeitos conforme necessário
        }
    }

    private void transformBlocksAroundPlayer(Player player) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = player.getLocation().add(x, -1, z).getBlock();
                if (block.getType() == Material.WATER) {
                    changedBlocks.put(block.getLocation(), block.getType());
                    block.setType(Material.ICE);

                    // Programa para reverter o bloco de volta para a água após um período
                    revertBlockLater(block);
                }
            }
        }
    }

    private void revertBlockLater(Block block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (changedBlocks.containsKey(block.getLocation())) {
                    block.setType(changedBlocks.get(block.getLocation()));
                    changedBlocks.remove(block.getLocation());
                }
            }
        }.runTaskLater(instance, 20 * 10); // Reverte após 10 segundos, por exemplo
    }
}

