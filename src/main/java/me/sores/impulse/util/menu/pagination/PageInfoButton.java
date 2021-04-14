package me.sores.impulse.util.menu.pagination;

import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by sores on 4/6/2021.
 */
public class PageInfoButton extends Button {

    private PaginatedMenu menu;

    public PageInfoButton(PaginatedMenu menu) {
        this.menu = menu;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        int pages = menu.getPages(player);

        return new ItemBuilder(Material.BOOK).setName("&ePage Info")
                .setLore(Arrays.asList(
                        ChatColor.YELLOW + "You are viewing page #" + menu.getPage() + ".",
                        ChatColor.YELLOW + (pages == 1 ? "There is 1 page." : "There are " + pages + " pages."),
                        "",
                        ChatColor.YELLOW + "Middle click here to",
                        ChatColor.YELLOW + "view all pages."
                )).build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
        }
    }

    public PaginatedMenu getMenu() {
        return menu;
    }
}
