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

import java.util.Objects;

import static org.murilonerdx.Hungergames.playersInGame;
import static org.murilonerdx.utils.SideBarUtils.setupSidebar;
import static org.murilonerdx.utils.SideBarUtils.sideBar;

public class PauseGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if(Hungergames.gameStartingEnder){
                if(strings.length == 2){
                    Player player = Bukkit.getPlayer(strings[0]);
                    if(player != null){
                        player.sendMessage(
                                ChatColor.RED + "O jogo foi brevemente pausado pra você por uma violação sua de: " + strings[2]
                        );
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 255, false, false));
                        player.setWalkSpeed(0.00f);
                    }

                    return true;
                }
                if(strings.length == 1){
                    Hungergames.startingGame = false;
                    Hungergames.gameStartingEnder = false;
                    playersInGame
                            .forEach(
                                    player -> {
                                        Player player1 = Bukkit.getPlayer(player);
                                        if(player != null){
                                            player1.setWalkSpeed(0.00f);
                                            player1.setSneaking(true);
                                            player1.sendMessage(
                                                    ChatColor.RED + "O jogo foi brevemente pausado pelos seguintes motivos " + strings[0]
                                            );

                                            player1.setInvulnerable(true);
                                        }

                                    }
                            );


                }

                if(strings.length == 0){
                    Hungergames.startingGame = false;
                    Hungergames.gameStartingEnder = false;
                    playersInGame
                            .forEach(
                                    player -> {
                                        Player player1 = Bukkit.getPlayer(player);
                                        if(player != null){
                                            player1.setWalkSpeed(0.00f);
                                            player1.sendMessage(
                                                    ChatColor.RED + "O jogo foi brevemente pausado encontramos algum problema "
                                            );

                                            player1.setInvulnerable(true);
                                        }

                                    }
                            );

                    return true;
                }


            }else{
                commandSender.sendMessage("Não foi possivel pausar o jogo");
            }

        }
        return false;
    }
}
