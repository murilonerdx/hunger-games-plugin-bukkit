package org.murilonerdx.listener;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.murilonerdx.Hungergames;


import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.murilonerdx.Hungergames.*;
import static org.murilonerdx.utils.ItemsUtils.createLightningProtectionItem;
import static org.murilonerdx.utils.ItemsUtils.createSpeedInvisibilityItem;


public class PlayerEventServerListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Optional<UUID> first = Hungergames.playersInGame.stream().filter(
                t -> t == event.getPlayer().getUniqueId()
        ).findFirst();
        if (first.isPresent()) {
            Bukkit.getBanList(BanList.Type.IP).addBan(event.getPlayer().getServer().getIp(),
                    ChatColor.RED + "Adeus para sempre aproveite sua realidade no mundo real.",
                    null,
                    null);
            event.getPlayer().kickPlayer(ChatColor.RED + "Você saiu durante o jogo, então você foi banido, caso você tenha saido do jogo por motivos como queda de energia e provar isso seu banimento é retiradoe");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Hungergames.setLastMovementTime(event.getPlayer());
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
    public void onPlayerJoin(PlayerJoinEvent event) throws ParseException {
        event.getPlayer().getInventory().clear();
        if (event.getPlayer().isOp()) {
            // Cria o item que você quer dar (por exemplo, uma bússola)
            ItemStack bucket = new ItemStack(Material.BUCKET, 1);


            // Dá o item ao jogador
            event.getPlayer().getInventory().addItem(bucket);
            event.getPlayer().getInventory().addItem(createSpeedInvisibilityItem());
            event.getPlayer().getInventory().addItem(createLightningProtectionItem());
        }
        for (PotionEffect activePotionEffect : event.getPlayer().getActivePotionEffects()) {
            event.getPlayer().removePotionEffect(activePotionEffect.getType());
        }

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
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        bossBar.removePlayer(Objects.requireNonNull(event.getEntity().getPlayer()));
        World world = Bukkit.getServer().createWorld(new WorldCreator("hurger-games"));

        event.getEntity().setHealth(20);
        event.getEntity().setHealthScale(20);
        event.getEntity().setFoodLevel(20);
        Hungergames.playersInGame.remove(Objects.requireNonNull(event.getEntity().getPlayer()).getUniqueId());
        Optional<UUID> uuid1 = Hungergames.playersInGame.stream().findFirst();

        if (uuid1.isPresent()) {
            Player player1 = Bukkit.getPlayer(uuid1.get());
            player1.setOp(true);
        }
        if (Hungergames.playersInGame.isEmpty() || Hungergames.playersInGame.size() == 1) {
            Hungergames.startingGame = false;
        }

        Arrays.stream(PotionEffectType.values()).forEach(
                t -> event.getEntity().removePotionEffect(t)
        );

        // Limpa os itens dropados
        event.getDrops().clear();
        // Se você também quiser limpar o inventário do jogador (opcional)
        event.getEntity().getInventory().clear();
        for (UUID uuid : Hungergames.playersInGame) {
            Player player = Bukkit.getPlayer(uuid);
            player.sendTitle(ChatColor.RED + "Um jogador morreu!!", ChatColor.YELLOW + "Nomeado de " + player.getName(), 10, 70, 20);
        }
        event.getEntity().teleport(world.getSpawnLocation());


//        event.getPlayer().kickPlayer(ChatColor.RED + "Você morreu para esse universo dentro do minecraft, foi banido permanentemente");

//        event.getPlayer().banPlayerIP("Adeus para sempre aproveite sua realidade no mundo real");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        System.out.println("Evento causado dano : " + event.getCause());
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
    }

    // Método para criar e abrir o inventário GUI
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.isOp() && event.getItem() != null && event.getItem().getType() == Material.BUCKET
                && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            Inventory gui = Bukkit.createInventory(null, 27, "Players nos jogos mortais");

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                ItemMeta meta = playerHead.getItemMeta();
                meta.setDisplayName(onlinePlayer.getName());
                playerHead.setItemMeta(meta);

                gui.addItem(playerHead);
            }

            player.openInventory(gui);
        }
    }

    // Método para tratar o clique no inventário e teleportar o operador
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Players nos jogos mortais") && event.getCurrentItem() != null) {
            Player operator = (Player) event.getWhoClicked();
            Player target = Bukkit.getPlayerExact(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));

            if (target != null) {
                operator.teleport(target.getLocation());
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

    // Método para atualizar os scores da sidebar
    private void updateScores(Scoreboard board, Objective objective, Player target) {
        board.clearSlot(DisplaySlot.SIDEBAR);
        objective = board.registerNewObjective("playerInfo" + System.currentTimeMillis(), "dummy", ChatColor.GOLD + "Info do Jogador");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(ChatColor.RED + "Vida").setScore((int) target.getHealth());
        objective.getScore(ChatColor.GREEN + "Fome").setScore( target.getFoodLevel());
        objective.getScore(ChatColor.YELLOW + "XP").setScore((int) target.getExp());
        objective.getScore(ChatColor.YELLOW +  target.getName()).setScore(target.getClientViewDistance());
        // Adicione mais informações aqui se necessário
    }
}