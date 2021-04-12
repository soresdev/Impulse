package me.sores.spark.util.menu.pagination;

import me.sores.spark.util.ItemBuilder;
import me.sores.spark.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by sores on 4/6/2021.
 */
public class PageButton extends Button {

    private int mod;
    private PaginatedMenu menu;

    public PageButton(int mod, PaginatedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                return new ItemBuilder(Material.PAPER).setName("&aNext Page").build();
            } else {
                return new ItemBuilder(Material.IRON_FENCE).setName("&cNext Page").build();
            }
        } else {
            if (hasPrevious(player)) {
                return new ItemBuilder(Material.PAPER).setName("&aPrevious Page").build();
            } else {
                return new ItemBuilder(Material.IRON_FENCE).setName("&cPrevious Page").build();
            }
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                this.menu.modPage(player, this.mod);
            } else {
            }
        } else {
            if (hasPrevious(player)) {
                this.menu.modPage(player, this.mod);
            } else {
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return this.menu.getPages(player) >= pg;
    }

    private boolean hasPrevious(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0;
    }

    public int getMod() {
        return mod;
    }

    public void setMod(int mod) {
        this.mod = mod;
    }

    public PaginatedMenu getMenu() {
        return menu;
    }

    public void setMenu(PaginatedMenu menu) {
        this.menu = menu;
    }
}
