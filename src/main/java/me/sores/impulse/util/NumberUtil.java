package me.sores.impulse.util;

import me.sores.impulse.Impulse;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by sores on 3/3/2020.
 */
public class NumberUtil {

    /**
     * Check if characters are numerical
     * @param str - String to check
     * @return
     */
    public static boolean isNumerical(String str) {
        return str.matches("[0-9]+");
    }

    public static int getInRangeDiff(int min, int max) {
        return Impulse.RAND.nextInt(max + 1 -min) + min;
    }

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
     * Check if a number can be a Double
     * @param str - String to check
     * @return
     */
    public static boolean isDouble(String str) {
        try {
            double i = Double.parseDouble(str);
        } catch (NumberFormatException ex) {
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
