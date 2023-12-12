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

            case "curse" -> {
                World world = locationWorld.getWorld();
                int x = locationWorld.getBlockX();
                int z = locationWorld.getBlockZ();
                int y = locationWorld.getWorld().getHighestBlockYAt(x, z);

                Location loc = new Location(locationWorld.getWorld(), x, y, z);

                spawnParticlesNearPlayer(loc, Particle.PORTAL, 2000);
                applyNauseaEffect(loc);

                List<Entity> entities = new ArrayList<>();
                for (int i = 0; i < 360; i += 18) { // Spawna em um círculo ao redor do jogador
                    double angle = Math.toRadians(i);
                    double xe = locationWorld.getX() + (5 * Math.cos(angle));
                    double ze = locationWorld.getZ() + (5 * Math.sin(angle));
                    Location spawnLocation = new Location(world, xe, locationWorld.getY(), ze);

                    Enderman enderman = (Enderman) world.spawnEntity(spawnLocation, EntityType.ENDERMAN);
                    enderman.setAI(false);
                    enderman.teleport(spawnLocation.setDirection(locationWorld.toVector().subtract(spawnLocation.toVector())));
                }
                freezePlayer(locationWorld, 60L);
                // Agendar para fazer os Endermans desaparecerem após 4 segundos
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        locationWorld.getWorld().getPlayers().forEach(player -> {

                            for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
                                if (entity instanceof Enderman) {
                                    entity.remove();
                                }
                            }

                            entities.forEach(Entity::remove);
                            removeNearbyEndermen(player, 100);

                            giveCursedItem(player);
                        });

                    }
                }.runTaskLater(Hungergames.instance, 50L);

            }
            case "storm-creeper" -> {
                startSuperStorm(locationWorld);
            }
            case "apocalipse" -> {
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
//            case "flood" -> {
//                // Inunda a localização do jogador temporariamente
//                floodAreaAroundPlayer(locationWorld);
//            }
            case "gravity" -> {
                // Aumenta a gravidade ao redor do jogador, tornando difícil se mover
                applyHeavyGravity(locationWorld);
            }
//            case "swarm" -> {
//                // Spawna uma nuvem de abelhas furiosas ao redor do jogador
//                spawnAngryBees(locationWorld);
//            }
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
