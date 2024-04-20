package org.murilonerdx;

import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.murilonerdx.initialize.InitializePlugin;

import java.util.*;

public final class Hungergames extends JavaPlugin {
    public static Hungergames instance;
    public static ScoreboardManager manager;
    public static Scoreboard board;
    public static Objective objective;
    public static List<UUID> playersInGame = new ArrayList<>();
    public static List<UUID> deadPlayers = new ArrayList<>();
    public static boolean gameStartingEnder = false;
    public static boolean startingGame = false;
    public static boolean gamePause = false;
    public static boolean gameRestart = false;
    public InitializePlugin<Hungergames> initializePlugin;
    public static BossBar bossBar;
    public static Map<UUID, Long> lastMovementTimes = new HashMap<>();
    public static Map<UUID, Double> playerGiftMoney = new HashMap<>();
    public static Map<UUID, Boolean> banPlayers = new HashMap<>();
    public static List<String> getPlayersNamesSponsors = new ArrayList<>();


    @Override
    public void onEnable() {
        instance = this;
        initializePlugin = new InitializePlugin<>(this);
        initializePlugin.executeCommands();
        initializePlugin.registerServerEvents();
        initializePlugin.schedulesThreadsView();

        saveDefaultConfig();
        reloadConfig();
        getPlayersNamesSponsors = getConfig().getStringList("sponsors");
        System.out.println("Configurações e sponsors carregador " + Arrays.toString(getPlayersNamesSponsors.toArray()));
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static World loadWorld(String worldName) {
        WorldCreator creator = new WorldCreator(worldName);
        return Bukkit.getServer().createWorld(creator);
    }

    public static void setLastMovementTime(Player player) {
        lastMovementTimes.put(player.getUniqueId(), System.currentTimeMillis());
    }


}

