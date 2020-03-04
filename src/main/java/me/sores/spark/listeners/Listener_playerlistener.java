package me.sores.spark.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by sores on 3/3/2020.
 */
public class Listener_playerlistener implements Listener {

    /**
     * Lapis enchant fix
     */

    @EventHandler
    public void onEnchant(EnchantItemEvent e) {
        e.getEnchanter().setLevel(e.getEnchanter().getLevel() - e.getExpLevelCost() + (e.whichButton() + 1));
        e.setExpLevelCost(0);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory().getType().equals(InventoryType.ENCHANTING)) {
            EnchantingInventory en = (EnchantingInventory)e.getInventory();
            en.setSecondary(new ItemStack(Material.INK_SACK, 64, (short)4));
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getType().equals(InventoryType.ENCHANTING)) {
            event.getInventory().setItem(1, null);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getType().equals(InventoryType.ENCHANTING)) {
            if (event.getRawSlot() == 1) {
                event.setCancelled(true);
            }
        }
    }

}
