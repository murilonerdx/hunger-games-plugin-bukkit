package org.murilonerdx.utils;

import org.bukkit.scoreboard.DisplaySlot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import java.util.UUID;

import static org.murilonerdx.Hungergames.*;

public class SideBarUtils {

    public static void updateSidebar(int value) {
        board.clearSlot(DisplaySlot.SIDEBAR);
        for (UUID player: playersInGame) {
            Player player1 = Bukkit.getPlayer(player);

            if(player1 != null){
                Score teamScore = objective.getScore(player1.getName());
                teamScore.setScore(Math.toIntExact(value));
            }
        }
    }

    public static void updateScore(String playerName, int newScore) {
        Score score = objective.getScore(playerName);
        score.setScore(newScore);
    }

    public static void sideBar(String title, String id) {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        objective = board.registerNewObjective(id, "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }


    public static void setupSidebar(String title, int scorePoint) {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();

        // Define o título e o critério do placar
        objective = board.registerNewObjective("HungerGames", "dummy", ChatColor.GOLD + "Jogadores status");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Exemplo de como adicionar linhas ao placar
        Score score = objective.getScore(title);
        score.setScore(scorePoint);

        // Define o placar para todos os jogadores online

        for (UUID player: playersInGame) {
            Player player1 = Bukkit.getPlayer(player);

            if(player1 != null){
                player1.setScoreboard(board);
            }
        }
    }
}
