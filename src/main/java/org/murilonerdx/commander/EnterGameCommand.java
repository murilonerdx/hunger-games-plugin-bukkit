package org.murilonerdx.commander;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.murilonerdx.Hungergames;

import java.util.Objects;

import static org.murilonerdx.utils.SideBarUtils.setupSidebar;


public class EnterGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if(Hungergames.gameStartingEnder){
                commandSender.sendMessage(
                        ChatColor.LIGHT_PURPLE + "Parabéns você acaba de entrar para os jogos não tem mais volta"
                );
                Hungergames.playersInGame.add(Objects.requireNonNull(((Player) commandSender).getPlayer()).getUniqueId());
                return true;
            }else{
                commandSender.sendMessage("O jogo ainda não foi iniciado para entrada");
            }

        }
        return false;
    }
}
