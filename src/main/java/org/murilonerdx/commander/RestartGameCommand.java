package org.murilonerdx.commander;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.murilonerdx.Hungergames;

import java.util.Objects;

import static org.murilonerdx.Hungergames.bossBar;
import static org.murilonerdx.utils.SideBarUtils.setupSidebar;


public class RestartGameCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			commandSender.sendMessage(
					ChatColor.LIGHT_PURPLE + "O jogo será restartado em breve"
			);
			Hungergames.playersInGame.clear();
			Hungergames.deadPlayers.clear();
			Hungergames.banPlayers.clear();
			Hungergames.gamePause = false;
			Hungergames.startingGame = false;
			Hungergames.gameStartingEnder = false;
			Hungergames.gameRestart = true;
			Bukkit.getOnlinePlayers().forEach(t -> bossBar.removePlayer(t));
			return true;
		}
		return false;
	}
}
