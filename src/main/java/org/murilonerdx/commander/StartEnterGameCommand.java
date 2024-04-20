package org.murilonerdx.commander;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.murilonerdx.Hungergames;

public class StartEnterGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(
                        ChatColor.GREEN + "Orientações \n \n" +
                                ChatColor.DARK_AQUA + "Você vai jogar jogos mortais, caso seja morto você será banido e nunca mais podera entrar nesse servidor \n \n" +
                                ChatColor.DARK_AQUA + "Se você ganhar, terá items adicionais nas proximas vezes que jogar além de premios em dinheiro \n \n" +
                                ChatColor.DARK_AQUA + "Saiba que não existe reembolso, nem formas de recuperar o que perdeu \n \n" +
                                ChatColor.DARK_AQUA + "A cada 1 minuto vai acontecer um evento, sendo 3 deles mortais, podendo acontecer no raio onde tem mais jogadores \n \n" +
                                ChatColor.DARK_AQUA + "Fique longe da barreira se você cair nela não conseguira sair, e vai tomar dano até morrer! \n \n"
                );

                if(!player.isOp()){
                    player.getInventory().clear();
                    player.setInvulnerable(false);
                    player.setGameMode(GameMode.SURVIVAL);
                }
                player.sendMessage(
                        ChatColor.RED + "DIGITE /enter para começar \n \n"
                );
            }
            Hungergames.gameStartingEnder = true;
            if(Hungergames.gameRestart){
                Hungergames.gameRestart = false;
            }

            return true;
        }
        return false;
    }


}
