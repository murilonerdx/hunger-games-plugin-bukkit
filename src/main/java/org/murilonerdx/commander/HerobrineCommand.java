package org.murilonerdx.commander;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.murilonerdx.Hungergames;
import org.murilonerdx.utils.EntityUtils;

import java.util.Arrays;
import java.util.Objects;

import static org.murilonerdx.utils.EntityUtils.*;

public class HerobrineCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Este comando só pode ser executado por um jogador.");
			return true;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			sender.sendMessage(ChatColor.AQUA + "Uso: /herobrine <comando> \n");
			sender.sendMessage(ChatColor.RED + "Uso: /herobrine spawnarmobs <tipo> <quantidade> \n ");
			sender.sendMessage(ChatColor.RED + "Uso: /herobrine raio <jogador> \n");
			sender.sendMessage(ChatColor.RED + "Uso: /herobrine tremores <jogador> \n ");
			sender.sendMessage(ChatColor.RED + "Uso: /herobrine teleportar <jogador> \n ");
			sender.sendMessage(ChatColor.RED + "Uso: /herobrine escuridao <jogador> \n ");
			sender.sendMessage(ChatColor.RED + "Uso: /herobrine visivel \n");
			sender.sendMessage(ChatColor.RED + "Uso: /herobrine milagre <nome_jogador> \n");
			sender.sendMessage(ChatColor.RED + "Uso: /herobrine knock <nome_jogador> \n");
			return true;
		}

		switch (args[0].toLowerCase()) {
			case "explode":
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Uso: /herobrine explode <jogador>");
					return true;
				}
				String nomeJogador = args[1];
				Player player1 = Bukkit.getPlayer(nomeJogador);
				explodeOnSummon(player1.getLocation().add(5, 5, 5), 2);
			case "knock":
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Uso: /herobrine knock <jogador>");
					return true;
				}
				String nomej = args[1];
				Player py = Bukkit.getPlayer(nomej);
				knockbackOnAttack(py, 2);
			case "milagre":
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Uso: /herobrine milagre <jogador>");
					return true;
				}

				String nomeJogador2 = args[1];
				Player player2 = Bukkit.getPlayer(nomeJogador2);
				healInRain(player2, 10);
			case "visivel":
				Arrays.stream(PotionEffectType.values()).forEach(
						player::removePotionEffect
				);
				try {
					Thread.sleep(5000);
					player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
					sender.sendMessage(ChatColor.GREEN + "Você se tornou invisível novamente!");
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			case "invisivel":
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
				sender.sendMessage(ChatColor.GREEN + "Você se tornou invisível!");
				break;
			case "spawnarmobs":
				if (args.length < 4) {
					sender.sendMessage(ChatColor.RED + "Uso: /herobrine spawnarmobs <tipo> <quantidade> <jogador>");
					return true;
				}
				String mobType = args[1];
				int qtd = Integer.parseInt(args[2]);
				EntityType entityType = EntityType.valueOf(mobType.toUpperCase());
				spawnEntitiesAround(Bukkit.getPlayer(args[3]), entityType, qtd);
				sender.sendMessage(ChatColor.GREEN + "Mobs " + mobType + " spawnados ao seu redor!");
				break;
			case "raio":
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Uso: /herobrine raio <jogador>");
					return true;
				}
				String nomeJogador3 = args[1];
				Player playerTarget = Bukkit.getPlayer(nomeJogador3);
				playerTarget.playSound(playerTarget.getLocation(), Sound.AMBIENT_CAVE, 5.0F, 5.0F);
				playerTarget.getWorld().strikeLightning(playerTarget.getLocation());
				break;
			case "tremores":
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Uso: /herobrine tremores <jogador>");
					return true;
				}
				String nomeJogador4 = args[1];
				Player playerTarget2 = Bukkit.getPlayer(nomeJogador4);
				playerTarget2.playSound(playerTarget2.getLocation(), Sound.AMBIENT_CAVE, 5.0F, 5.0F);
				int range = 10;
				for (int i = -range; i <= range; i++) {
					for (int j = -range; j <= range; j++) {
						Location loc = playerTarget2.getLocation().clone().add(i, 0, j);
						loc.getWorld().strikeLightningEffect(loc);
					}
				}
				sender.sendMessage(ChatColor.GREEN + "Tremores foram sentidos na área!");
				break;
			case "teleportar":
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Uso: /herobrine teleportar <jogador>");
					return true;
				}
				Player targetPlayer = Bukkit.getPlayer(args[1]);
				if (targetPlayer != null) {
					targetPlayer.teleport(player.getLocation().clone().add(5, 0, 5));
					sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " foi teleportado para perto de você!");
				} else {
					sender.sendMessage(ChatColor.RED + "Jogador não encontrado.");
				}
				break;
			case "escuridao":
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Uso: /herobrine escuridao <jogador>");
					return true;
				}
				Player darkPlayer = Bukkit.getPlayer(args[1]);
				darkPlayer.playSound(darkPlayer.getLocation(), Sound.AMBIENT_CAVE, 5.0F, 5.0F);
				if (darkPlayer != null) {
					darkPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 30, 1));
					sender.sendMessage(ChatColor.GREEN + "Escuridão repentina ocorreu ao redor de " + darkPlayer.getName() + "!");
				} else {
					sender.sendMessage(ChatColor.RED + "Jogador não encontrado.");
				}
				break;
			default:
				sender.sendMessage(ChatColor.RED + "Comando desconhecido.");
				break;
		}

		return true;
	}

}
