package me.sores.spark.util.menu;

import com.google.common.collect.Maps;
import javafx.util.Pair;
import me.sores.spark.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by sores on 1/18/2021.
 */
public class Menu implements InventoryHolder {

    private final ConcurrentMap<Integer, MenuItem> items;
    private String title;
    private final int rows;
    private final boolean openInventory;
    public Object extra;
    private boolean exitOnClickOutside;
    private MenuAPI.MenuCloseBehaviour menuCloseBehaviour;
    private Menu parentMenu;
    private Inventory inventory;
    private int currentPage;
    private int maxPage;
    private Map<Integer, List<Pair<Integer, MenuItem>>> pageItems;
    private List<Integer> pageSlots;

    public Menu(String title, int rows, boolean openInventory) {
        this(title, rows, null, openInventory);
    }

    private Menu(String title, int rows, Menu parentMenu, boolean openInventory) {
        this.items = Maps.newConcurrentMap();
        this.exitOnClickOutside = false;
        this.title = StringUtil.color(title);
        this.rows = rows;
        this.parentMenu = parentMenu;
        this.openInventory = openInventory;
        extra = null;
    }

    public MenuAPI.MenuCloseBehaviour getMenuCloseBehaviour() {
        return this.menuCloseBehaviour;
    }

    public void setMenuCloseBehaviour(MenuAPI.MenuCloseBehaviour menuCloseBehaviour) {
        this.menuCloseBehaviour = menuCloseBehaviour;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public Map<Integer, List<Pair<Integer, MenuItem>>> getPageItems() {
        return pageItems;
    }

    public void setPageItems(Map<Integer, List<Pair<Integer, MenuItem>>> pageItems) {
        this.pageItems = pageItems;
    }

    public List<Integer> getPageSlots() {
        return pageSlots;
    }

    public void setPageSlots(List<Integer> pageSlots) {
        this.pageSlots = pageSlots;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean nextPage(Player player) {
        if (currentPage >= maxPage) return false;
        currentPage++;
        clearPageSlots();
        for (Pair<Integer, MenuItem> pair : pageItems.get(currentPage)) {
            addMenuItem(pair.getValue(), pair.getKey());
        }
        updateMenu();
        return true;
    }

    public boolean previousPage(Player player) {
        if (currentPage <= 1) return false;
        currentPage--;
        clearPageSlots();
        for (Pair<Integer, MenuItem> pair : pageItems.get(currentPage)) {
            addMenuItem(pair.getValue(), pair.getKey());
        }
        updateMenu();
        return true;
    }

    public void clearPageSlots() {
        for (Integer slot : pageSlots) {
            removeMenuItem(slot);
        }
    }

    public void setupPages(List<MenuItem> items, List<Integer> pageSlots) {
        currentPage = 1;
        pageItems = new HashMap<>();
        this.pageSlots = new ArrayList<>();
        this.pageSlots.addAll(pageSlots);
        if (items.size() <= pageSlots.size()) {
            maxPage = 1;
            List<Pair<Integer, MenuItem>> itemSlotList = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                itemSlotList.add(new Pair<>(pageSlots.get(i), items.get(i)));
            }
            pageItems.put(1, itemSlotList);
        } else {
            int pagesWholeNumber = items.size() / pageSlots.size();
            if (pagesWholeNumber * pageSlots.size() == items.size()) {
                maxPage = pagesWholeNumber;
            } else {
                maxPage = pagesWholeNumber + 1;
            }
            int count = 0;
            int page = 1;
            pageItems.put(1, new ArrayList<>());
            for (MenuItem item : items) {
                if (count == pageSlots.size()) {
                    count = 0;
                    page++;
                    pageItems.put(page, new ArrayList<>());
                }
                pageItems.get(page).add(new Pair<>(pageSlots.get(count), item));
                count++;
            }
        }
        setupInitialPage();
    }

    private void setupInitialPage() {
        clearPageSlots();
        for (Pair<Integer, MenuItem> pair : pageItems.get(currentPage)) {
            addMenuItem(pair.getValue(), pair.getKey());
        }
    }


    private void setExitOnClickOutside(boolean exit) {
        this.exitOnClickOutside = exit;
    }

    public Map<Integer, MenuItem> getMenuItems() {
        return this.items;
    }

    public boolean addMenuItem(MenuItem item, int x, int y) {
        return this.addMenuItem(item, y * 9 + x);
    }

    public MenuItem getMenuItem(int index) {
        return this.items.get(index);
    }

    public boolean addMenuItem(MenuItem item, int index) {
        ItemStack slot = this.getInventory().getItem(index);
        if (slot != null && slot.getType() != Material.AIR) {
            this.removeMenuItem(index);
        }
        item.setSlot(index);
        this.getInventory().setItem(index, item.getItemStack());
        this.items.put(index, item);
        item.addToMenu(this);
        return true;
    }

    public boolean removeMenuItem(int x, int y) {
        return this.removeMenuItem(y * 9 + x);
    }

    public boolean removeMenuItem(int index) {
        ItemStack slot = this.getInventory().getItem(index);
        if (slot == null || slot.getType().equals(Material.AIR)) {
            return false;
        }
        this.getInventory().clear(index);
        this.items.remove(index).removeFromMenu(this);
        return true;
    }

    boolean selectMenuItem(Player player, int index, ClickType clickType) {
        if (this.items.containsKey(index)) {
            MenuItem item = this.items.get(index);
            item.onClick(player, clickType);
            return item.isClickable();
        }

        return true;
    }

    public boolean isOpenInventory() {
        return openInventory;
    }

    public void openMenu(Player player) {
        if (!this.getInventory().getViewers().contains(player)) {
            player.openInventory(this.getInventory());
        }
    }

    public void closeMenu(Player player) {
        if (this.getInventory().getViewers().contains(player)) {
            this.getInventory().getViewers().remove(player);
            player.closeInventory();
        }
    }

    public Menu getParent() {
        return this.parentMenu;
    }

    public void setParent(Menu menu) {
        this.parentMenu = menu;
    }

    public Inventory getInventory() {
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(this, this.rows * 9, this.title);
        }
        return this.inventory;
    }

    public void fillRange(int startingIndex, int endingIndex, MenuItem menuItem) throws IndexOutOfBoundsException {
        if (endingIndex <= startingIndex)
            throw new IndexOutOfBoundsException("fillRange() : Ending index must be less than starting index.");
        if (startingIndex < 0 || startingIndex > (rows * 9) - 1)
            throw new IndexOutOfBoundsException("fillRange() : Starting index is outside inventory.");
        if (endingIndex > rows * 9 - 1)
            throw new IndexOutOfBoundsException("fillRange() : Ending index is outside inventory.");
        for (int i = startingIndex; i <= endingIndex; i++)
            addMenuItem(menuItem, i);
    }

    public boolean exitOnClickOutside() {
        return this.exitOnClickOutside;
    }

    @Override
    protected Menu clone() {
        Menu clone = new Menu(this.title, this.rows, this.isOpenInventory());
        clone.setExitOnClickOutside(this.exitOnClickOutside);
        clone.setMenuCloseBehaviour(this.menuCloseBehaviour);
        for (Map.Entry<Integer, MenuItem> entry : this.items.entrySet()) {
            clone.addMenuItem(entry.getValue(), entry.getKey());
        }
        return clone;
    }

    public void redoInventory() {
        this.inventory = Bukkit.createInventory(this, this.rows * 9, this.title);
        for (Map.Entry<Integer, MenuItem> entry : this.items.entrySet()) {
            addMenuItem(entry.getValue(), entry.getKey());
        }
    }

    public void updateMenu() {
        for (Map.Entry<Integer, MenuItem> entry : items.entrySet()) {
            if (!entry.getValue().isClickable()) continue;
            this.getInventory().setItem(entry.getKey(), entry.getValue().getItemStack());
        }
        for (HumanEntity entity : this.getInventory().getViewers()) {
            ((Player) entity).updateInventory();
        }
    }

    public ConcurrentMap<Integer, MenuItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Menu{title=" + title + ", rows=" + rows + ", exitOnClickOutside=" + exitOnClickOutside + ", parentMenu=" + parentMenu.toString() + ", inventory=" + inventory.toString() + ", currentPage=" + currentPage + ", maxPage=" + maxPage + ", pageItems=" + pageItems + "pageSlots=" + pageSlots + "}";
    }

}
