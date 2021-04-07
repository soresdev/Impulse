package me.sores.spark.util.menu.buttons;

import me.sores.spark.util.ItemBuilder;
import me.sores.spark.util.menu.Button;
import me.sores.spark.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by sores on 4/6/2021.
 */
public class BackButton extends Button {

    private Menu back;

    public BackButton(Menu back) {
        this.back = back;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE).setName("&cGo Back").build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Button.playNeutral(player);
        back.openMenu(player);
    }

}
