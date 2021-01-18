package me.sores.spark.util.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by sores on 1/18/2021.
 */
public abstract class MenuItem {

    private Menu menu;
    private int slot;
    private boolean clickable = true;

    void addToMenu(Menu menu) {
        this.menu = menu;
    }

    void removeFromMenu(Menu menu) {
        if (this.menu == menu) {
            this.menu = null;
        }
    }

    public Menu getMenu() {
        return this.menu;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public abstract void onClick(Player player, ClickType clickType);

    public abstract ItemStack getItemStack();

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public static class UnclickableMenuItem extends MenuItem {

        @Override
        public void onClick(Player player, ClickType clickType) {

        }

        @Override
        public ItemStack getItemStack() {
            return null;
        }
    }

}
