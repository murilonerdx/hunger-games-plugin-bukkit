package org.murilonerdx.commander;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.murilonerdx.Hungergames;

import java.util.List;
import java.util.UUID;

import static org.murilonerdx.listener.PlayerEventServerListener.getRandomLocation;

public class StartGameCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			if (!Hungergames.startingGame && !Hungergames.gamePause && Hungergames.playersInGame.size() >= 2) {
				try {
					titleStartGame();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				World world = Hungergames.loadWorld("hurger-games");
				if (world == null) {
					world = Bukkit.getServer().createWorld(new WorldCreator("hurger-games"));
				}

				world.setDifficulty(Difficulty.HARD);
				world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);

				WorldBorder border = world.getSpawnLocation().getWorld().getWorldBorder();
				border.setCenter(new Location(world, 0, 100, 0)); // Centro da borda
				border.setSize(800); // Tamanho inicial da borda (raio em blocos)
				Hungergames.startingGame = true;
				Hungergames.gameStartingEnder = false;

				for (UUID uuid : Hungergames.playersInGame) {
					Player player = Bukkit.getPlayer(uuid);
					player.setHealth(20);
					player.setInvulnerable(false);
					player.setExp(0);
					player.setHealthScale(60);
					player.setFoodLevel(20);
					player.teleport(getRandomLocation(border.getCenter(), 400));
				}

				new BukkitRunnable() {
					@Override
					public void run() {
						if (Hungergames.startingGame && !Hungergames.gamePause) {
							if(Hungergames.gameRestart){
								this.cancel();
								return;
							}
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
			}else{
				commandSender.sendMessage("Sem jogadores suficientes");
				return false;
			}
		}
		return false;
	}

	public void titleStartGame() throws InterruptedException {
		List<UUID> playersInGame = Hungergames.playersInGame;
		var ref = new Object() {
			int count = 5;
		};
		while (ref.count != -1) {
			Thread.sleep(1000);
			playersInGame.forEach(player -> {
				Player player1 = Bukkit.getPlayer(player);
				if (player1 != null) {
					player1.sendTitle(ChatColor.GREEN + "O jogo começara em", ref.count + " segundos", 10, 40, 20);
				}

			});

			ref.count = ref.count - 1;

		}
	}
}
