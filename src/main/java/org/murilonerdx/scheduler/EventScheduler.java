package org.murilonerdx.scheduler;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.murilonerdx.Hungergames;
import org.murilonerdx.utils.EventsUtil;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;
import static org.murilonerdx.Hungergames.*;
import static org.murilonerdx.Hungergames.setLastMovementTime;
import static org.murilonerdx.utils.PlayerUtils.*;

public class EventScheduler extends BukkitRunnable {
    private final Random random;
    private int time = 1 * 120;

    public EventScheduler() {
        this.random = new Random();
    }

    public void checkForImmobilePlayers() {
        long currentTime = System.currentTimeMillis();
        for (UUID uuid : Hungergames.playersInGame) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null){
                long lastMoveTime = lastMovementTimes.getOrDefault(player.getUniqueId(), 0L);
                if (currentTime - lastMoveTime > 5 * 60 * 1000) { // 5 minutos em milissegundos
                    // Cai um raio no jogador
                    player.getWorld().strikeLightning(player.getLocation());
                    // Reseta o contador de tempo para o jogador
                    setLastMovementTime(player);
                }
            }
        }
    }

    @Override
    public void run() {
        if (startingGame && !gamePause) {
            getServer().getScheduler().scheduleSyncRepeatingTask(Hungergames.instance, this::checkForImmobilePlayers, 20L, 20L);
//            getServer().getScheduler().scheduleSyncRepeatingTask(Hungergames.instance, () -> {
//                for (UUID uuid : Hungergames.playersInGame) {
//                    Player player = Bukkit.getPlayer(uuid);
//                    if (player != null) {
//                        checkDarkness(player);
//                    }
//                }
//            }, 0L, 20L);



            if (time <= 0) {
                String[] events = {
                        "zeus", "zeus-around", "curse", "storm-creeper", "storm-water-force",
                        "storm-fire", "storm-water", "gravity", "blindness","apocalipse",
                        "ice_trap", "wither_effect", "poisonous_fog", "zombie_attack", "skeleton_attack",
                        "storm-poisonous"
                };

                String chosenEvent = events[random.nextInt(events.length)];
                System.out.println("Novo evento adicionado: " + chosenEvent);
                executeEvent(chosenEvent);
                time = 120;
            } else {
                time--;
                if (Hungergames.bossBar != null) {
                    double progress = (double) time / 120;
                    Hungergames.bossBar.setProgress(progress);
                    Hungergames.bossBar.setTitle(ChatColor.RED + "Próximo evento em: " + time + " segundos");
                } else {
                    bossBar = Bukkit
                            .createBossBar(ChatColor.DARK_PURPLE + "Proximo evento em 2 minutos", BarColor.BLUE, BarStyle.SOLID);
                    bossBar.setVisible(true);
                    bossBar.setStyle(BarStyle.SOLID);
                    bossBar.setProgress((double) time / 120);
                }

            }
        }
    }



    private void executeEvent(String event) {
        Location location = findHighestDensityLocation();
        createChestWithItems(location);
        bossBar.removeAll();
        bossBar = Bukkit
                .createBossBar(ChatColor.DARK_PURPLE + event, BarColor.BLUE, BarStyle.SOLID);

        bossBar.setVisible(true);
        bossBar.setStyle(BarStyle.SOLID);

        Objects.requireNonNull(location.getWorld()).getPlayers().forEach(bossBar::addPlayer);

        System.out.println("Novo evento começou: " + event);
        EventsUtil.punishCommandPlayer(location, event);
    }

    public void newBoosBarUpdate(String event) {
        bossBar.setTitle(ChatColor.DARK_PURPLE + event);
        bossBar.setProgress((double) time / (60));
    }
}
