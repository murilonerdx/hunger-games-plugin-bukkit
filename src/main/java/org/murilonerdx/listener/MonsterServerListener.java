package org.murilonerdx.listener;

import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.murilonerdx.Hungergames;
import org.murilonerdx.utils.ItemsUtils;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static org.murilonerdx.Hungergames.bossBar;
import static org.murilonerdx.Hungergames.instance;
import static org.murilonerdx.utils.ItemsUtils.*;


public class MonsterServerListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(Hungergames.startingGame){
            LivingEntity entity = event.getEntity();

            // Verifica se a entidade é um monstro
            if (entity instanceof Monster) {
                // Gera um item aleatório e o adiciona aos drops
                event.getDrops().add(generateRandomItem());
            }
        }
    }

    private ItemStack generateRandomItem() {
        // Obtém todos os valores de Material
        List<Material> materials =  Arrays.stream(Material.values()).filter(ItemsUtils::selectItems).toList();

        // Escolhe um Material aleatório, garantindo que é um item (não bloqueio ou ar)
        Material randomMaterial;
        do {
            randomMaterial = materials.get(new Random().nextInt(materials.size()));
        } while (!randomMaterial.isItem() || randomMaterial.isAir());

        // Retorna um novo ItemStack do material escolhido
        return new ItemStack(randomMaterial, 1);
    }
}