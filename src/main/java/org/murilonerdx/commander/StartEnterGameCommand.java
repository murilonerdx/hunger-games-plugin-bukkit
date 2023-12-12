package org.murilonerdx.commander;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
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
                        ChatColor.RED + "Orientações principais por favor leia com atenção \n" +
                                ChatColor.GREEN + "Você vai jogar jogos mortais, caso seja morto você será banido e nunca mais podera entrar nesse servidor \n" +
                                ChatColor.YELLOW + "Se você ganhar, terá items adicionais nas proximas vezes que jogar além de premios em dinheiro \n" +
                                ChatColor.DARK_PURPLE + "Saiba que não existe reembolso, nem formas de recuperar o que perdeu \n" +
                                ChatColor.GOLD + "Bem vindo aos jogos mortais, e que você tenha uma excelente morte ou vida \n" +
                                ChatColor.RED + "A cada 1 minuto vai acontecer um evento, sendo 3 deles mortais, podendo acontecer no raio onde tem mais jogadores \n" +
                                ChatColor.GOLD + "Fique longe da barreira se você cair nela não conseguira sair, e vai tomar dano até morrer! \n"
                );

                player.sendMessage(
                        ChatColor.RED + "DIGITE /enter para começar \n"
                );
            }
            Hungergames.gameStartingEnder = true;


            return true;
        }
        return false;
    }


}
