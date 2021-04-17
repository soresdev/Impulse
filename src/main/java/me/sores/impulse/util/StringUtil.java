package me.sores.impulse.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;

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

    public static String joinList(String seperator, Object... list) {
        StringBuilder buf = new StringBuilder();

        for (Object each : list) {
            if (buf.length() > 0) {
                buf.append(seperator);
            }

            if (each instanceof Collection) {
                buf.append(joinList(seperator, ((Collection)each).toArray()));
            }
            else {
                try {
                    buf.append(each.toString());
                }
                catch (Exception e) {
                    buf.append(each.toString());
                }
            }
        }
        return buf.toString();
    }

    public static String joinListSkip(String seperator, String skip, Object... list) {
        StringBuilder buf = new StringBuilder();

        for (Object each : list) {
            if (each.toString().equalsIgnoreCase(skip)) {
                continue;
            }

            if (buf.length() > 0) {
                buf.append(seperator);
            }

            if (each instanceof Collection) {
                buf.append(joinListSkip(seperator, skip, ((Collection)each).toArray()));
            }
            else {
                try {
                    buf.append(each.toString());
                }
                catch (Exception e) {
                    buf.append(each.toString());
                }
            }
        }

        return buf.toString();
    }

    /**
     * Default usage string
     * @return
     */
    public static String getDefaultUsage() { return ChatColor.YELLOW + "Usage: "; }

    public static void log(String message){
        Bukkit.getConsoleSender().sendMessage(color(message));
    }

    public static String intToString(int intt){
        return String.valueOf(intt);
    }
}
