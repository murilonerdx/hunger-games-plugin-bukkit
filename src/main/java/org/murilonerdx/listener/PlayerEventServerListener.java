package org.murilonerdx.listener;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.murilonerdx.Hungergames;
import org.murilonerdx.scheduler.BootsEffectTask;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

import static org.murilonerdx.Hungergames.*;
import static org.murilonerdx.utils.ItemsUtils.*;
import static org.murilonerdx.utils.PlayerUtils.createIceCube;


public class PlayerEventServerListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Optional<String> stringStream = Hungergames.getPlayersNamesSponsors.stream().filter(t -> event.getPlayer().getName().equals(t)).findFirst();

		if (startingGame && !playersInGame.isEmpty() && stringStream.isEmpty() && !event.getPlayer().isOp()) {
			if (playersInGame.contains(event.getPlayer().getUniqueId())) {
				banPlayers.put(event.getPlayer().getUniqueId(), true);
				playersInGame.remove(event.getPlayer().getUniqueId());
				event.getPlayer().ban(
						ChatColor.RED + "Você foi punido por sair durante o jogo, está banido desse mundo para sempre!",
						new Date(),
						null,
						true
				);
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		ItemStack boots = player.getInventory().getBoots();


		if (boots != null && (boots.isSimilar(createFireBoots())
				|| boots.isSimilar(createwWaterIceBoots()) || boots.isSimilar(createJumpBoots())

		)

		) {
			player.setWalkSpeed(0.7f); // Aumenta a velocidade de movimento

			// Inicia a tarefa que mantém os efeitos
			new BootsEffectTask(player, boots).runTaskTimer(instance, 0L, 20L * 5); // A cada 5 segundos
		} else {
			player.setWalkSpeed(0.2f); // Restaura a velocidade de caminhada padrão
		}

	}

	@EventHandler
	public void onPlayerMoveOrPause(PlayerMoveEvent event) {
		if (gamePause) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMoveBorder(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		WorldBorder border = world.getWorldBorder();
		Location to = event.getTo();

		if (to != null && !border.isInside(to)) {
			player.damage(3.0); // Aplica 2 corações de dano
//            event.setCancelled(true); // Opcional: cancela a movimentação do jogador
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (startingGame) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerJoinBan(PlayerJoinEvent event) throws ParseException {
		if (!banPlayers.isEmpty() && banPlayers.containsKey(event.getPlayer().getUniqueId()) && !event.getPlayer().isOp()) {
			event.getPlayer().kickPlayer(ChatColor.RED + "Você está banido!");
		}

		if (gameStartingEnder) {
			event.getPlayer().sendMessage(
					ChatColor.RED + "Orientações principais por favor leia com atenção \n \n" +
							ChatColor.GREEN + "Você vai jogar jogos mortais, caso seja morto você será banido e nunca mais podera entrar nesse servidor \n \n" +
							ChatColor.YELLOW + "Se você ganhar, terá items adicionais nas proximas vezes que jogar além de premios em dinheiro \n \n" +
							ChatColor.DARK_PURPLE + "Saiba que não existe reembolso, nem formas de recuperar o que perdeu \n \n" +
							ChatColor.GOLD + "Bem vindo aos jogos mortais, e que você tenha uma excelente morte ou vida \n \n" +
							ChatColor.RED + "A cada 1 minuto vai acontecer um evento, sendo 3 deles mortais, podendo acontecer no raio onde tem mais jogadores \n \n" +
							ChatColor.GOLD + "Fique longe da barreira se você cair nela não conseguira sair, e vai tomar dano até morrer! \n \n"
			);

			event.getPlayer().sendMessage(
					ChatColor.RED + "DIGITE /enter para começar \n \n"
			);
		}
	}

	public static Location getRandomLocation(Location center, int radius) {
		// Gera coordenadas aleatórias dentro do raio especificado
		double randomX = center.getX() + (Math.random() * (2 * radius) - radius);
		double randomZ = center.getZ() + (Math.random() * (2 * radius) - radius);

		// Obtém a coordenada Y do bloco mais alto nessa posição
		int maxY = center.getWorld().getHighestBlockYAt((int) randomX, (int) randomZ);

		// Cria a localização aleatória dentro do raio especificado
		return new Location(center.getWorld(), randomX, maxY, randomZ);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) throws ParseException {
		if(gameRestart){
			event.getPlayer().teleport(Hungergames.loadWorld("word").getSpawnLocation());
		}
		if (!startingGame) {
			if (!deadPlayers.contains(event.getPlayer().getUniqueId())) {
				Optional<UUID> first = Hungergames.deadPlayers.stream().filter(
						t -> t == event.getPlayer().getUniqueId()
				).findFirst();

				if (first.isPresent()) {
					Bukkit.getBanList(BanList.Type.IP).addBan(event.getPlayer().getServer().getIp(),
							ChatColor.RED + "Adeus para sempre aproveite sua realidade no mundo real.",
							null,
							null);
					event.getPlayer().kickPlayer(ChatColor.RED + "Você morreu para esse universo dentro do minecraft, foi banido permanentemente");
				}
				for (PotionEffect activePotionEffect : event.getPlayer().getActivePotionEffects()) {
					event.getPlayer().removePotionEffect(activePotionEffect.getType());
				}
			} else {
				Bukkit.getBanList(BanList.Type.IP).addBan(event.getPlayer().getServer().getIp(),
						ChatColor.RED + "Adeus para sempre aproveite sua realidade no mundo real.",
						new Date(),
						null);
			}
			event.getPlayer().setInvulnerable(true);
			event.getPlayer().getInventory().clear();
			System.out.println("Bem vindo " + event.getPlayer().getName());
			Optional<String> stringStream = Hungergames.getPlayersNamesSponsors.stream().filter(t -> event.getPlayer().getName().equals(t)).findFirst();

			if (stringStream.isPresent()) {
				Player player = Bukkit.getPlayer(stringStream.get());
				if (!player.getInventory().contains(Material.BUCKET)) {
					ItemStack bucket = new ItemStack(Material.BUCKET, 1);
					event.getPlayer().getInventory().addItem(bucket);
				}

			} else {
				event.getPlayer().getInventory().remove(Material.BUCKET);
			}
		} else {
			event.getPlayer().kickPlayer("O jogo já começou tente mais tarde ou no proximo evento hunger games");
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (startingGame && playersInGame.contains(event.getEntity().getUniqueId())) {
			banPlayers.put(event.getEntity().getUniqueId(), true);
			deadPlayers.add(Objects.requireNonNull(event.getEntity().getPlayer()).getUniqueId());
			bossBar.removePlayer(Objects.requireNonNull(event.getEntity().getPlayer()));

			event.getEntity().setHealth(4);
			event.getEntity().setHealthScale(20);
			event.getEntity().setFoodLevel(20);

			if (playersInGame.size() >= 2) {
				Hungergames.playersInGame.remove(Objects.requireNonNull(event.getEntity().getPlayer()).getUniqueId());
			}

			if (playersInGame.isEmpty()) {
				startingGame = false;
			}
			if (playersInGame.size() == 1) {
				Optional<UUID> uuid1 = Hungergames.playersInGame.stream().findFirst();
				if (uuid1.isPresent()) {
					Player player1 = Bukkit.getPlayer(uuid1.get());
					player1.getWorld().setPVP(false);
					player1.setInvulnerable(true);

					Player player = Bukkit.getPlayer(playersInGame.get(0));
					player.sendMessage(ChatColor.RED + "Parabéns você venceu o jogo!, seu dinheiro será enviado, cadastresse com o codigo" + ChatColor.DARK_PURPLE + " WIN-" + player.getName() + "-" + LocalDateTime.now() + new Random().nextInt());

					gamePause = true;
					startingGame = false;
					gameStartingEnder = true;

					World word = loadWorld("word");
					Bukkit.getOnlinePlayers().forEach(p -> bossBar.removePlayer(p));
					Bukkit.getOnlinePlayers().forEach(p -> p.teleport((word.getSpawnLocation())));

				}

				for (UUID uuid : Hungergames.playersInGame) {
					Player player = Bukkit.getPlayer(uuid);
					player.sendTitle(ChatColor.RED + "Um jogador morreu!!", ChatColor.YELLOW + "Nomeado de " + event.getEntity().getName(), 10, 40, 20);
				}
			}
			removePotionsEffects(event);

			// Limpa os itens dropados
			event.getDrops().clear();
			event.getEntity().getInventory().clear();
			event.getEntity().kickPlayer(ChatColor.RED + "Você morreu para esse universo dentro do minecraft, foi banido permanentemente");
		}
	}

	private static void removePotionsEffects(PlayerDeathEvent event) {
		Arrays.stream(PotionEffectType.values()).forEach(
				t -> event.getEntity().removePotionEffect(t)
		);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {

			Player player = (Player) event.getEntity();
			System.out.println("Contains player item : " + player.getInventory().contains(createLightningProtectionItem()));
			System.out.println("Contains player item : " + player.getInventory().toString());

			if (player.getInventory().contains(createLightningProtectionItem()) && event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING ||
					player.getInventory().contains(createLightningProtectionItem()) && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
					player.getInventory().contains(createLightningProtectionItem()) && event.getCause() == EntityDamageEvent.DamageCause.LAVA
			) {
				event.setCancelled(true); // Cancela o dano de raio
			}
		}
	}

	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event) {
		if (event.getItem().equals(createSpeedInvisibilityItem())) {
			// Aplica os efeitos por 2 minutos
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2400, 1));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2400, 1));
		}

		if (event.getItem().equals(createItemCustomCure())) {
			// Aplica os efeitos por 2 minutos
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 2400, 2));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 2400, 1));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 2400, 1));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 800, 3));
		}
	}


	// Método para criar e abrir o inventário GUI
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Optional<String> stringStream = Hungergames.getPlayersNamesSponsors.stream().filter(t -> event.getPlayer().getName().equals(t)).findFirst();

		Player player = event.getPlayer();
		if (stringStream.isPresent() && event.getItem() != null && event.getItem().getType() == Material.BUCKET
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || player.isOp() && event.getItem() != null && event.getItem().getType() == Material.BUCKET
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))) {

			Inventory gui = Bukkit.createInventory(null, 27, "Players nos jogos mortais");
			List<UUID> playersInGame1 = playersInGame;
			playersInGame1.remove(player.getUniqueId());
			for (UUID onlinePlayer : playersInGame1) {
				Player player1 = Bukkit.getPlayer(onlinePlayer);

				ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
				ItemMeta meta = playerHead.getItemMeta();

				meta.setDisplayName(Objects.requireNonNull(player1).getDisplayName());
				playerHead.setItemMeta(meta);

				gui.addItem(playerHead);
			}

			player.openInventory(gui);
		}

		if (event.getItem() != null && event.getItem().equals(createItemCustomSword())
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			createIceCube(event.getClickedBlock().getWorld(), event.getClickedBlock().getLocation());

		}
	}

	// Método para tratar o clique no inventário e teleportar o operador
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getView().getTitle().equals("Players nos jogos mortais") && event.getCurrentItem() != null) {
			Player operator = (Player) event.getWhoClicked();
			Player target = Bukkit.getPlayerExact(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));

			if (target != null) {
				if(!operator.isOp()){
					operator.setGameMode(GameMode.SPECTATOR);
					operator.setSpectatorTarget(target);
				}else{
					operator.teleport(target);
				}
//				operator.teleport(target.getLocation());
				updateSidebar(operator, target);
			}
			event.setCancelled(true);
		}
	}

	// Método para atualizar a sidebar
	private void updateSidebar(Player operator, Player target) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("playerInfo" + System.currentTimeMillis(), "dummy", ChatColor.GOLD + "Info do Jogador");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		operator.setScoreboard(board);

		new BukkitRunnable() {
			@Override
			public void run() {
				if (!operator.isOnline()) {
					this.cancel();
					operator.setScoreboard(manager.getNewScoreboard());
				} else {
					updateScores(board, objective, target);
				}
			}
		}.runTaskTimer(instance, 0L, 20L);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (!getPlayersNamesSponsors.contains(event.getPlayer().getDisplayName()) || event.getPlayer().isOp()) {
			// Cancela o evento se o jogador não tiver permissão para usar o chat
			event.setCancelled(false);
			event.getPlayer().sendMessage("Você não tem permissão para usar o chat!");
		}
	}


	// Método para atualizar os scores da sidebar
	private void updateScores(Scoreboard board, Objective objective, Player target) {
		board.clearSlot(DisplaySlot.SIDEBAR);
		objective = board.registerNewObjective("playerInfo" + System.currentTimeMillis(), "dummy", ChatColor.GOLD + "Info do Jogador");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		objective.getScore(ChatColor.RED + "Vida").setScore((int) target.getHealth());
		objective.getScore(ChatColor.GREEN + "Fome").setScore(target.getFoodLevel());
		objective.getScore(ChatColor.YELLOW + "XP").setScore((int) target.getExp());
		objective.getScore(ChatColor.YELLOW + target.getName()).setScore(target.getClientViewDistance());
		// Adicione mais informações aqui se necessário
	}

}