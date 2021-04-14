package me.sores.impulse.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by sores on 3/3/2020.
 */
public class NumberUtil {

    /**
     * Formats numbers by like 1000000 to 1,000,000
     * @param str - String to format
     * @return
     */
    public static String formatNumber(String str) {
        int amount = Integer.parseInt(str);
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }

    /**
     * Check if a String is a number
     * @param str - String to check
     * @return
     */
    public static boolean isNumber(String str){
        try {
            int i = Integer.parseInt(str);
        } catch (NumberFormatException ex){
            return false;
        }
        return true;
    }

    /**
     * Gets proper NumberFormat
     * @return
     */
    public static NumberFormat getProperFormat() { return new DecimalFormat("##.###"); }



}
