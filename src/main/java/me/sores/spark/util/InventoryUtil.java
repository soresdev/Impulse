package me.sores.spark.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by sores on 3/11/2020.
 */
public class InventoryUtil {

    public static void giveItemSafely(Player player, ItemStack item) {
        if(player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
        else {
            player.getInventory().addItem(item);
        }
    }

    public static boolean isPlayerHolding(Player player, Material material) {
        return player.getItemInHand() != null && player.getItemInHand().getType() == material;
    }

    public static boolean hasItem(Inventory inventory, ItemStack compare) {
        for(ItemStack is : inventory) {
            if(is == null) continue;

            if(is == compare) return true;
        }
        return false;
    }

    public static boolean hasItem(Player player, ItemStack compare) {
        return hasItem(player.getInventory(), compare);
    }

    public static boolean isPotion(ItemStack item) {
        if (item.getType() == Material.POTION) {
            return true;
        }
        return false;
    }

    public static ItemStack[] fixInventoryOrder(ItemStack[] source) {
        ItemStack[] fixed = new ItemStack[36];

        System.arraycopy(source, 0, fixed, 27, 9);
        System.arraycopy(source, 9, fixed, 0, 27);

        return fixed;
    }

}
