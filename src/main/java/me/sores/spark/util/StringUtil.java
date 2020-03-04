package me.sores.spark.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Created by sores on 3/3/2020.
 */
public class StringUtil {

    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void log(String message){
        Bukkit.getConsoleSender().sendMessage(color(message));
    }

    public static String intToString(int intt){
        return String.valueOf(intt);
    }
}
