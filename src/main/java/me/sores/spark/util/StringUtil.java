package me.sores.spark.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sores on 3/3/2020.
 */
public class StringUtil {

    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static List<String> color(List<String> strings) {
        List<String> colors = new ArrayList<>();
        for (String s : strings)
            colors.add(color(s));
        return colors;
    }

    /**
     * Check if characters are alphabetical
     * @param str - String to check
     * @return
     */
    public static boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }

    /**
     * Check if characters are alphabetical with numbers
     * @param str - String to check
     * @return
     */
    public static boolean isAlphaWithNumber(String str) {
        return str.matches("[a-zA-Z0-9]+");
    }


    public static void log(String message){
        Bukkit.getConsoleSender().sendMessage(color(message));
    }

    public static String intToString(int intt){
        return String.valueOf(intt);
    }
}
