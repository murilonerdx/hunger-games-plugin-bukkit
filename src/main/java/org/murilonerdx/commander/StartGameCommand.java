package org.murilonerdx.commander;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.murilonerdx.Hungergames;

import java.util.UUID;

public class StartGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if(!Hungergames.startingGame && !Hungergames.gamePause){
                World world = Bukkit.getServer().createWorld(new WorldCreator("hurger-games"));

                WorldBorder border = world.getSpawnLocation().getWorld().getWorldBorder();
                border.setCenter(new Location(world, 0, 100, 0)); // Centro da borda
                border.setSize(500); // Tamanho inicial da borda (raio em blocos)
                Hungergames.startingGame = true;

                for (UUID uuid: Hungergames.playersInGame) {
                    Player player = Bukkit.getPlayer(uuid);
                    player.setHealth(20);
                    player.setHealthScale(60);
                    player.setFoodLevel(20);
                    player.teleport(world.getSpawnLocation());
                }


                // Agendar a redução da borda
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(Hungergames.startingGame && !Hungergames.gamePause){
                            double newSize = border.getSize() * 0.9; // Reduz em 10%
                            if (newSize >= 50) { // Tamanho mínimo
                                border.setSize(newSize, 60); // Reduz a borda gradualmente
                            } else {
                                this.cancel(); // Para a tarefa quando atingir o tamanho mínimo
                            }
                        }
                    }
                }.runTaskTimer(Hungergames.instance, 0L, 20L * 60); // Executa a cada minuto

                return true;
            }
        }
        return false;
    }
}
