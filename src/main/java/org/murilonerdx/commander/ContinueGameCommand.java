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

import java.util.UUID;

import static org.murilonerdx.Hungergames.gamePause;
import static org.murilonerdx.Hungergames.playersInGame;

public class ContinueGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (!Hungergames.gameStartingEnder) {
                Hungergames.startingGame = true;
                Hungergames.gameStartingEnder = true;
                gamePause = false;
                for (UUID uuid : playersInGame) {
                    Player player = Bukkit.getPlayer(uuid);

                    if (player != null) {
                        player.sendMessage(
                                ChatColor.RED + "Não encontramos nenhum problema!"
                        );
                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                        player.setWalkSpeed(0.2f);
                        player.setInvulnerable(false);
                        player.setSneaking(false);
                    }
                }
                return true;
            } else {
                commandSender.sendMessage("Não foi possivel continuar o jogo");
                return false;
            }

        }
        return false;
    }
}
