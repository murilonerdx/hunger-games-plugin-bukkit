package org.murilonerdx.utils;

import org.bukkit.scoreboard.DisplaySlot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.murilonerdx.Hungergames;

import java.util.UUID;

import static org.murilonerdx.Hungergames.*;

public class SideBarUtils {
    public static void updateSidebar(int value) {
        if (board != null) { // Verifica se board não é null antes de usá-lo
            board.clearSlot(DisplaySlot.SIDEBAR);
            for (UUID player: playersInGame) {
                Player player1 = Bukkit.getPlayer(player);

                if(player1 != null){
                    Score teamScore = objective.getScore(player1.getName());
                    teamScore.setScore(Math.toIntExact(value));
                }
            }
        }
    }

    public static void updateScore(String playerName, int newScore) {
        if (board != null && objective != null) { // Verifica se board e objective não são null antes de usá-los
            Score score = objective.getScore(playerName);
            score.setScore(newScore);
        }
    }

    public static void sideBar(String title, String id) {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        objective = board.registerNewObjective(id, "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }


    public static void setupSidebar(String title, int scorePoint) {
        if (Hungergames.board == null) { // Verifica se o scoreboard ainda não foi criado
            Hungergames.manager = Bukkit.getScoreboardManager();
            Hungergames.board = manager.getNewScoreboard();
        }

        if (Hungergames.objective == null) { // Verifica se o objetivo ainda não foi criado
            Hungergames.objective = Hungergames.board.registerNewObjective("HungerGames", "dummy", ChatColor.GOLD + "Jogadores status");
            Hungergames.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        if (board != null) { // Verifica se board não é null antes de usá-lo
            // Define o título e o critério do placar
            Hungergames.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            // Exemplo de como adicionar linhas ao placar
            Score score = Hungergames.objective.getScore(title);
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
}
