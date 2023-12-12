package org.murilonerdx.listener;

import org.bukkit.*;
import org.bukkit.entity.Entity;
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
import static org.murilonerdx.utils.EntityUtils.*;
import static org.murilonerdx.utils.ItemsUtils.*;


public class MonsterServerListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (Hungergames.startingGame) {
            LivingEntity entity = event.getEntity();
            explodeOnDeath(entity, 1);
//            deathStrikeLightning(event.getEntity(), 1);
            // Verifica se a entidade é um monstro
            if (entity instanceof Monster) {
                // Tenta gerar um item raro
                ItemStack rareItem = generateRareItem();
                if (rareItem != null) {
                    event.getDrops().add(rareItem);
                } else {
                    // Se nenhum item raro for gerado, gera um item comum
                    event.getDrops().add(generateRandomItem());
                }
            }
        }
    }

    private ItemStack generateRareItem() {
        Random rand = new Random();
        int chance = rand.nextInt(100) + 1; // Gera um número entre 1 e 100

        if (chance >= 10 && chance <= 30) {
            return createLightningProtectionItem(); // 10% de chance
        } else if (chance >= 40 && chance <= 60) { // Adicional de 30%, totalizando 40%
            return createItemCustomCure();
        } else if (chance >= 90) { // Adicional de 40%, totalizando 80%
            return createItemCustomSword();
        } else if (chance >= 70 && chance <= 80) { // Adicional de 40%, totalizando 80%
            return createFireBoots() ;
        } else if (chance >= 81 && chance <= 85) { // Adicional de 40%, totalizando 80%
            return createFireBoots() ;
        }
        else {
            return null; // 20% de chance de não gerar um item raro
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