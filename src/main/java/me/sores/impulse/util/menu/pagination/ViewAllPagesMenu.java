package me.sores.impulse.util.menu.pagination;

import me.sores.impulse.util.menu.Button;
import me.sores.impulse.util.menu.Menu;
import me.sores.impulse.util.menu.buttons.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sores on 4/6/2021.
 */
public class ViewAllPagesMenu extends Menu {

    private PaginatedMenu menu;

    public ViewAllPagesMenu(PaginatedMenu menu) {
        this.menu = menu;
    }

    @Override
    public String getTitle(Player player) {
        return "Jump to page";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(menu));

        int index = 10;

        for (int i = 1; i <= menu.getPages(player); i++) {
            buttons.put(index++, new JumpToPageButton(i, menu, menu.getPage() == i));

            if ((index - 8) % 9 == 0) {
                index += 2;
            }
        }

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

}
