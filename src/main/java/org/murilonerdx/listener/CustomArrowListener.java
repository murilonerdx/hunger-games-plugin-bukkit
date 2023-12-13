package org.murilonerdx.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.murilonerdx.Hungergames;
import org.murilonerdx.utils.EntityUtils;
import org.murilonerdx.utils.ItemsUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.murilonerdx.utils.EntityUtils.explodeOnDeath;
import static org.murilonerdx.utils.ItemsUtils.*;


public class CustomArrowListener implements Listener {

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getProjectile();
            ItemStack bow = event.getBow();
            System.out.println("Atirou uma flecha");
            System.out.println("BOW NAME: " + bow.getItemMeta().getDisplayName());

            if (bow != null && bow.hasItemMeta() && bow.getItemMeta().hasDisplayName()) {
                String bowName = bow.getItemMeta().getDisplayName();

                // Exemplo: Adicionando metadata com base no nome do arco
                if (bowName.equals("IceWallBow")) {
                    arrow.setMetadata("IceWallArrow", new FixedMetadataValue(Hungergames.instance, true));
                } else if (bowName.equals("LightningBow")) {
                    arrow.setMetadata("LightningArrow", new FixedMetadataValue(Hungergames.instance, true));
                } else if (bowName.equals("ExplosiveBow")) {
                    arrow.setMetadata("ExplosiveArrow", new FixedMetadataValue(Hungergames.instance, true));
                    System.out.println("ExplosiveArrow disparada"); // Log para confirmação
                }
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            Entity hit = event.getHitEntity();
            if (arrow.hasMetadata("IceWallArrow")) {
                if (hit != null) {
                    createIceWall(hit.getLocation());
                }
            } else if (arrow.hasMetadata("ExplosiveArrow")) {
                arrow.getLocation().getWorld().createExplosion(arrow.getLocation(), 2.0F);
                isPlayerExecute((Player) event.getHitEntity(), "ExplosiveArrow");

            } else if (arrow.hasMetadata("LightningArrow")) {
                arrow.getLocation().getWorld().strikeLightning(arrow.getLocation());
                isPlayerExecute((Player) event.getHitEntity(), "LightningArrow");
            }
        }
    }

    public static void isPlayerExecute(Player player, String metaData) {
        if (player != null) {
            if (metaData.equals("ExplosiveArrow")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 100, true, true), true);
            }
            if (metaData.equals("LightningArrow")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, 200, true, true), true);
            }
        }
    }

    private void createIceWall(Location target) {
        for (int x = -2; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    Block block = target.clone().add(x, y, z).getBlock();
                    if (block.getType() == Material.AIR) {
                        block.setType(Material.ICE);
                    }
                }
            }
        }
    }


}