package me.sores.impulse.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by sores on 4/14/2021.
 */
public class MessageUtil {

    public static boolean message(CommandSender sender, String string){
        if(sender == null || string.isEmpty()){
            return false;
        }

        if(sender instanceof Player){
            Player player = (Player) sender;

            player.sendMessage(StringUtil.color(string));
            return true;
        }else{
            StringUtil.log(string);
            return true;
        }

    }

    public static boolean message(Player player, String string){
        if(player == null || string.isEmpty()){
            return false;
        }

        player.sendMessage(StringUtil.color(string));
        return true;
    }

    public static boolean message(List<Player> players, String string){
        if(players == null || players.isEmpty() || string.isEmpty()){
            return false;
        }

        for(Player player : players){
            player.sendMessage(StringUtil.color(string));
        }

        return true;
    }

    public static boolean message(Player[] players, String string){
        if(players == null || players.length == 0 || string.isEmpty()) {
            return false;
        }

        for(Player player : players){
            player.sendMessage(StringUtil.color(string));
        }

        return true;
    }

    public static boolean noPermission(CommandSender sender){
        message(sender, ChatColor.RED + "You don't have permission to use this command.");
        return true;
    }

    public static void sendList(CommandSender target, String[] messages) {
        for(String message : messages) {
            message(target, message);
        }
    }

    public static String buildString(String[] args, int startArgs){
        StringBuilder sb = new StringBuilder();
        String string;
        for(int i = startArgs; i < args.length; i++){
            sb.append(args[i]).append(" ");
        }
        string = sb.toString();
        return string;
    }

}
