package org.murilonerdx.initialize;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.murilonerdx.Hungergames;
import org.murilonerdx.commander.*;
import org.murilonerdx.listener.CursedItemListener;
import org.murilonerdx.listener.CustomArrowListener;
import org.murilonerdx.listener.MonsterServerListener;
import org.murilonerdx.listener.PlayerEventServerListener;
import org.murilonerdx.scheduler.EventScheduler;

import java.util.Objects;

import static org.murilonerdx.utils.ItemsUtils.recipeCustomCraft;


public class InitializePlugin<T extends JavaPlugin> {
    private final T principal;

    public InitializePlugin(T principal) {
        this.principal = principal;
    }

    public void executeCommands() {
        Objects.requireNonNull(principal.getCommand("enter")).setExecutor(new EnterGameCommand());
        Objects.requireNonNull(principal.getCommand("startgame")).setExecutor(new StartGameCommand());
        Objects.requireNonNull(principal.getCommand("punish")).setExecutor(new PunishCommand());
        Objects.requireNonNull(principal.getCommand("herobrine")).setExecutor(new HerobrineCommand());
        Objects.requireNonNull(principal.getCommand("enter-start")).setExecutor(new StartEnterGameCommand());
        Objects.requireNonNull(principal.getCommand("pause")).setExecutor(new PauseGameCommand());
        Objects.requireNonNull(principal.getCommand("continue")).setExecutor(new ContinueGameCommand());
        Objects.requireNonNull(principal.getCommand("hard-reset")).setExecutor(new RestartGameCommand());

        initialCraft();
    }

    public void initialCraft(){
        recipeCustomCraft();
    }

    public void registerServerEvents(){
        principal.getServer().getPluginManager().registerEvents(new PlayerEventServerListener(), principal);
        principal.getServer().getPluginManager().registerEvents(new MonsterServerListener(), principal);
        principal.getServer().getPluginManager().registerEvents(new CursedItemListener(), principal);
        principal.getServer().getPluginManager().registerEvents(new CustomArrowListener(), principal);
    }

    public void schedulesThreadsView(){
        EventScheduler scheduler = new EventScheduler();
        scheduler.runTaskTimer(Hungergames.instance, 0L, 20L);
    }

}

