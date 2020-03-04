package me.sores.spark.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Created by sores on 3/3/2020.
 */
public class ItemUtil {

    public static ItemStack getPlayerHead(String name) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta)head.getItemMeta();
        meta.setOwner(name);
        head.setItemMeta(meta);

        return head;
    }

    public static boolean isHelmet(Material material) {
        return material == Material.LEATHER_HELMET || material == Material.GOLD_HELMET || material == Material.IRON_HELMET ||
                material == Material.CHAINMAIL_HELMET || material == Material.DIAMOND_HELMET;
    }

    public static boolean isChestPlate(Material material) {
        return material == Material.LEATHER_CHESTPLATE || material == Material.GOLD_CHESTPLATE || material == Material.IRON_CHESTPLATE ||
                material == Material.CHAINMAIL_CHESTPLATE || material == Material.DIAMOND_CHESTPLATE;
    }

    public static boolean isLegging(Material material) {
        return material == Material.LEATHER_LEGGINGS || material == Material.GOLD_LEGGINGS || material == Material.IRON_LEGGINGS ||
                material == Material.CHAINMAIL_LEGGINGS || material == Material.DIAMOND_LEGGINGS;
    }

    public static boolean isBoot(Material material) {
        return material == Material.LEATHER_BOOTS || material == Material.GOLD_BOOTS || material == Material.IRON_BOOTS ||
                material == Material.CHAINMAIL_BOOTS || material == Material.DIAMOND_BOOTS;
    }

    public static boolean isArmor(Material material) {
        return isHelmet(material) || isChestPlate(material) || isLegging(material) || isBoot(material);
    }

    public static boolean isLeatherArmor(Material material) {
        return material == Material.LEATHER_HELMET || material == Material.LEATHER_CHESTPLATE ||
                material == Material.LEATHER_LEGGINGS || material == Material.LEATHER_BOOTS;
    }

    public static boolean isGoldArmor(Material material) {
        return material == Material.GOLD_HELMET || material == Material.GOLD_CHESTPLATE ||
                material == Material.GOLD_LEGGINGS || material == Material.GOLD_BOOTS;
    }

    public static boolean isIronArmor(Material material) {
        return material == Material.IRON_HELMET || material == Material.IRON_CHESTPLATE ||
                material == Material.IRON_LEGGINGS || material == Material.IRON_BOOTS;
    }

    public static boolean isChainMailArmor(Material material) {
        return material == Material.CHAINMAIL_HELMET || material == Material.CHAINMAIL_CHESTPLATE ||
                material == Material.CHAINMAIL_LEGGINGS || material == Material.CHAINMAIL_BOOTS;
    }

    public static boolean isDiamondArmor(Material material) {
        return material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE ||
                material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS;
    }

    public static boolean isPickaxe(Material material) {
        return material == Material.WOOD_PICKAXE || material == Material.GOLD_PICKAXE || material == Material.IRON_PICKAXE || material == Material.DIAMOND_PICKAXE;
    }

    public static boolean isAxe(Material material) {
        return material == Material.WOOD_AXE || material == Material.GOLD_AXE || material == Material.IRON_AXE || material == Material.DIAMOND_AXE;
    }

    public static boolean isShovel(Material material) {
        return material == Material.WOOD_SPADE || material == Material.GOLD_SPADE || material == Material.IRON_SPADE || material == Material.DIAMOND_SPADE;
    }

    public static boolean isHoe(Material material) { //no u da ho
        return material == Material.WOOD_HOE || material == Material.GOLD_HOE || material == Material.IRON_HOE || material == Material.DIAMOND_HOE;
    }

    public static boolean isTool(Material material) {
        return isPickaxe(material) || isAxe(material) || isShovel(material) || isHoe(material) || material == Material.SHEARS;
    }

    public static boolean isSword(Material material) {
        return material == Material.WOOD_SWORD || material == Material.STONE_SWORD || material == Material.GOLD_SWORD || material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD;
    }

    public static boolean isWeapon(Material material) {
        return isSword(material) || material == Material.BOW;
    }
}
