package me.sores.spark.util.menu;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

/**
 * Created by sores on 1/18/2021.
 */
public class MenuAPI implements Listener {

    public Menu cloneMenu(Menu menu) throws CloneNotSupportedException {
        return menu.clone();
    }

    public void removeMenu(Menu menu) {
        for (HumanEntity viewer : menu.getInventory().getViewers()) {
            if (viewer instanceof Player) {
                menu.closeMenu((Player) viewer);
            } else {
                viewer.closeInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMenuItemClicked(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof Menu) {
            event.setCancelled(true);
            ((Player) event.getWhoClicked()).updateInventory();
            Menu menu = (Menu) inventory.getHolder();
            if (menu.isOpenInventory()) return;
            if (!(event.getWhoClicked() instanceof Player)) {
                return;
            }
            Player player = (Player) event.getWhoClicked();
            if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                if (menu.exitOnClickOutside()) {
                    menu.closeMenu(player);
                }
            } else {
                int index = event.getRawSlot();
                if (index < inventory.getSize()) {
                    if (event.getAction() != InventoryAction.NOTHING) {
                        if (!menu.selectMenuItem(player, index, ClickType.valueOf(event.getClick().name()))) {
                            event.setCancelled(false);
                        }
                    }
                } else {
                    if (menu.exitOnClickOutside()) {
                        menu.closeMenu(player);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMenuClosed(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Inventory inventory = event.getInventory();
            if (inventory.getHolder() instanceof Menu) {
                Menu menu = (Menu) inventory.getHolder();
                MenuCloseBehaviour menuCloseBehaviour = menu.getMenuCloseBehaviour();
                if (menuCloseBehaviour != null) {
                    menuCloseBehaviour.onClose((Player) event.getPlayer(), menu);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLogoutCloseMenu(PlayerQuitEvent event) {
        if (!(event.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof Menu)) {
            return;
        }
        Menu menu = (Menu) event.getPlayer().getOpenInventory().getTopInventory().getHolder();
        menu.setMenuCloseBehaviour(null);
        event.getPlayer().closeInventory();
    }

    public interface MenuCloseBehaviour {
        void onClose(Player player, Menu menu);
    }

}
