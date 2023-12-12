package org.murilonerdx.commander;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.murilonerdx.Hungergames;

import static org.murilonerdx.Hungergames.playersInGame;

public class ContinueGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if(!Hungergames.gameStartingEnder){
                if(strings.length == 1){
                    Player player = Bukkit.getPlayer(strings[0]);
                    if(player != null){
                        player.sendMessage(
                                ChatColor.RED + "Não encontramos nenhum problema!"
                        );
                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                        player.setWalkSpeed(0.2f);
                        player.setInvulnerable(false);
                        player.setSneaking(false);
                    }

                    return true;
                }
                if(strings.length == 2){
                    Hungergames.startingGame = true;
                    Hungergames.gameStartingEnder = true;
                    playersInGame
                            .forEach(
                                    player -> {
                                        Player player1 = Bukkit.getPlayer(player);
                                        if(player != null){
                                            player1.setWalkSpeed(0.20f);
                                            player1.setSneaking(false);
                                            player1.sendMessage(
                                                    ChatColor.RED + "O jogo voltou ao normal "
                                            );

                                            player1.setInvulnerable(false);
                                        }

                                    }
                            );

                    return true;

                }

            }else{
                commandSender.sendMessage("Não foi possivel continuar o jogo");
            }

        }
        return false;
    }
}
