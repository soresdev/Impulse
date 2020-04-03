package me.sores.spark.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

    public static String trimList(List<String> list) {
        if(list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator i = list.iterator();
        if(i.hasNext()){
            sb.append(i.next());
            while(i.hasNext()){
                sb.append(',').append(i.next());
            }
        }
        return sb.toString();
    }

    public static String[] trimList(String[] array, int startFrom) {
        List<String> trimd = Arrays.asList(array).subList(startFrom, array.length);

        return trimd.toArray(new String[trimd.size()]);
    }


    public static void log(String message){
        Bukkit.getConsoleSender().sendMessage(color(message));
    }

    public static String intToString(int intt){
        return String.valueOf(intt);
    }
}
