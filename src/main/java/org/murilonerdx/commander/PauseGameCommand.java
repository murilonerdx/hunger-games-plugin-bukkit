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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.murilonerdx.Hungergames.gamePause;
import static org.murilonerdx.Hungergames.playersInGame;

public class PauseGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (Hungergames.startingGame) {
                gamePause = true;
                System.out.println(strings.length);
                if (strings.length == 1) {
                    Player player = Bukkit.getPlayer(strings[0]);
                    if (player != null) {
                        player.sendMessage(
                                ChatColor.RED + "O jogo foi brevemente pausado pra você por uma violação sua de: " + strings[1]
                        );
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 255, false, false));
                        player.setWalkSpeed(0.00f);

                        return true;

                    } else {
                        commandSender.sendMessage("Player não encontrado");
                    }
                }
                if (strings.length == 1) {
                    playersInGame
                            .forEach(
                                    player -> {
                                        Player player1 = Bukkit.getPlayer(player);
                                        if (player != null) {
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

                if (strings.length == 0) {
                    playersInGame
                            .forEach(
                                    player -> {
                                        Player player1 = Bukkit.getPlayer(player);
                                        if (player1 != null) {
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


            } else {
                commandSender.sendMessage("Não foi possivel pausar o jogo");
                return false;
            }

        }
        return false;
    }

}
