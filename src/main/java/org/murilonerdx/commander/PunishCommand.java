package org.murilonerdx.commander;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.murilonerdx.Hungergames;
import org.murilonerdx.utils.EntityUtils;
import org.murilonerdx.utils.PlayerUtils;

import java.util.Objects;
import java.util.Random;

import static org.murilonerdx.utils.PlayerUtils.floodAreaAroundPlayer;


public class PunishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if(Hungergames.startingGame){
               final Random random = new Random();

                Player player = Bukkit.getPlayer(strings[0]);
                String[] events = {
                        "zombie", "light","spawn-creatures"
                };
                String chosenEvent = events[random.nextInt(events.length)];

                if(strings[1] != null){
                    chosenEvent = strings[1];
                }

                switch (chosenEvent){
                    case "water-floor" ->{
                        floodAreaAroundPlayer(player.getLocation());
                    }
                    case "zombie" -> {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2, 100));
                        EntityUtils.spawnEntitiesAround(player, EntityType.ZOMBIE, 20);
                    }
                    case "light" -> {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2, 100));
                        PlayerUtils.strikePlayerWithLightning(player.getLocation());
                    }
                    case "spawn-creatures" -> {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2, 100));
                        EntityUtils.spawnEntitiesAround(player, EntityType.ZOMBIE, 10);
                        EntityUtils.spawnEntitiesAround(player, EntityType.CREEPER, 10);
                    }

                }
                return true;
            }else{
                commandSender.sendMessage("O jogo ainda não foi iniciado para entrada");
            }

        }
        return false;
    }
}
