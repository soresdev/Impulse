package me.sores.spark.util.menu;

import me.sores.spark.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by sores on 3/3/2020.
 */
public abstract class BasicMenu {

    protected static Inventory toCreate;
    private Player holder;

    public BasicMenu(Player holder) {
        this.holder = holder;

        toCreate = Bukkit.createInventory(holder, getSize(), StringUtil.color(getTitle()));
    }

    public abstract int getSize();
    public abstract String getTitle();

    public abstract void setup();
    public abstract void setup(Player player);

    public abstract void open(Player player);

}
