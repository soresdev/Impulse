package me.sores.spark.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

}
