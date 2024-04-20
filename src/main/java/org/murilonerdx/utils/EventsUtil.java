package org.murilonerdx.utils;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.murilonerdx.Hungergames;

import java.util.ArrayList;
import java.util.List;

import static org.murilonerdx.utils.PlayerUtils.*;

public class EventsUtil {
    public static void punishCommandPlayer(Location locationWorld, String command) {
        switch (command.toLowerCase()) {
            case "zeus" -> {
                locationWorld.getWorld().getPlayers().forEach(
                        p -> {
                            p.playSound(locationWorld, Sound.AMBIENT_CAVE, 5.0F, 5.0F);
                            spawnParticlesNearPlayer(p.getLocation(), Particle.PORTAL, 2000);
                            strikePlayerWithLightning(locationWorld);
                        }
                );

            }

            case "herobrine" -> {
                locationWorld.getWorld().getPlayers().forEach(
                        p -> {
                            p.sendMessage("Herobrine foi liberado");
                        }
                );
            }
            case "zeus-around" -> {
                World world = locationWorld.getWorld();
                Location center = locationWorld;

                applyNauseaEffect(locationWorld);
                // Inicia a chuva
                world.setStorm(true);

                // Raio cai em um círculo de 8 blocos de raio
                int radius = 8;
                for (int i = 0; i < 360; i += 30) { // Aumente o passo para menos raios, diminua para mais
                    double angle = Math.toRadians(i);
                    double x = center.getX() + (radius * Math.cos(angle));
                    double z = center.getZ() + (radius * Math.sin(angle));
                    Location strikeLocation = new Location(world, x, center.getY(), z);
                    world.strikeLightning(strikeLocation);
                }
                strikePlayerWithLightning(locationWorld);
            }
            case "storm-creeper" -> {
                startSuperStorm(locationWorld);
            }
            case "apocalipse" -> {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.RED + "FUJA!"));
                startWaterStorm(locationWorld, 1);
                startCustomized(locationWorld, Particle.LAVA, Material.LAVA);
                startCustomized(locationWorld, Particle.FALLING_WATER, Material.WATER);
            }
            case "storm-fire" -> {
                startCustomized(locationWorld, Particle.LAVA, Material.LAVA);
            }
            case "storm-water" -> {
                startCustomized(locationWorld, Particle.FALLING_WATER, Material.WATER);
            }
            case "gravity" -> {
                // Aumenta a gravidade ao redor do jogador, tornando difícil se mover
                applyHeavyGravity(locationWorld);
            }
            case "ice_trap" -> {
                // Prende o jogador em um cubo de gelo
                trapPlayerInIce(locationWorld);
            }
            case "wither_effect" -> {
                // Aplica o efeito Wither ao jogador
                applyWitherEffect(locationWorld);
            }
            case "poisonous_fog" -> {
                // Cria uma névoa venenosa ao redor do jogador
                createPoisonousFog(locationWorld);
            }
            case "zombie_attack" -> {
                // Faz com que Endermen ataquem o jogador
                summonZombieAttack(locationWorld);
            }
            case "skeleton_attack" -> {
                // Faz com que Endermen ataquem o jogador
                summonSkeletonAttack(locationWorld);
            }
            case "storm-poisonous" -> {
                locationWorld.getWorld().setStorm(true);
                locationWorld.getWorld().setThundering(true);
                locationWorld.getWorld().setWeatherDuration(1200); // Duração da tempestade em ticks (5 minutos)
                locationWorld.getWorld().setThunderDuration(1200);
                // Faz com que Endermen ataquem o jogador
                startPoisonRain(locationWorld);
            }
        }
    }

}
