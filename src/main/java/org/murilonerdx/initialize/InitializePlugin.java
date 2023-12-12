package org.murilonerdx.initialize;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.murilonerdx.Hungergames;
import org.murilonerdx.commander.EnterGameCommand;
import org.murilonerdx.commander.PunishCommand;
import org.murilonerdx.commander.StartEnterGameCommand;
import org.murilonerdx.commander.StartGameCommand;
import org.murilonerdx.listener.PlayerEventServerListener;
import org.murilonerdx.scheduler.EventScheduler;

import java.util.Objects;


public class InitializePlugin<T extends JavaPlugin> {
    private final T principal;

    public InitializePlugin(T principal) {
        this.principal = principal;
    }

    public void executeCommands() {
        Objects.requireNonNull(principal.getCommand("enter")).setExecutor(new EnterGameCommand());
        Objects.requireNonNull(principal.getCommand("startgame")).setExecutor(new StartGameCommand());
        Objects.requireNonNull(principal.getCommand("punish")).setExecutor(new PunishCommand());
        Objects.requireNonNull(principal.getCommand("enter-start")).setExecutor(new StartEnterGameCommand());

    }

    public void registerServerEvents(){
        principal.getServer().getPluginManager().registerEvents(new PlayerEventServerListener(), principal);
    }

    public void schedulesThreadsView(){
        EventScheduler scheduler = new EventScheduler();
        scheduler.runTaskTimer(Hungergames.instance, 0L, 20L);


    }

}

