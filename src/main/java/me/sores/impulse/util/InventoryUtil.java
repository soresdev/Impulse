package me.sores.impulse.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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

    public static void removeIfMatches(Inventory inventory, ItemStack... item) {
        List<ItemStack> rem = new ArrayList<>();
        for(ItemStack is : inventory) {
            if(is == null || is.getType() == Material.AIR) continue;
            Material compareTo = is.getType();
            String compareTit = getItemTitle(is);

            for(ItemStack remove : item) {
                if(remove.getType() == compareTo && is.getDurability() == remove.getDurability() && getItemTitle(remove).equalsIgnoreCase(compareTit)) {
                    rem.add(remove);
                }
            }
        }
        if(!rem.isEmpty()) {
            for(ItemStack is : rem) {
                inventory.removeItem(is);
            }
        }
    }

    public static String getItemTitle(ItemStack is) {
        return is.hasItemMeta() && is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() : "";
    }

    public static int firstNotFull(Material material, Inventory inventory) {
        for(int slot = 0; slot <= inventory.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);

            if(item == null || item.getType() == Material.AIR) return slot;
            if(item.getType() == material && item.getAmount() < item.getMaxStackSize()) return slot;
        }

        return -1;
    }

    public static void setArmorContentSlot(Player player, int slot, ItemStack item) {
        ItemStack[] contents = player.getInventory().getArmorContents();
        contents[slot] = item;
        player.getInventory().setArmorContents(contents);
    }

    public static void replaceItem(Player player, boolean closeInventory, Material replace, ItemStack replaceWith) {
        if(closeInventory) player.closeInventory();

        for(int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getContents()[i];
            if(item != null && item.getType() == replace) {
                player.getInventory().setItem(i, replaceWith);
                return;
            }
        }
    }

    /**
     * Check if a player has inventory space for item
     *
     * @param inventory - Inventory to check if ItemStack fits
     * @param is        - ItemStack to check if it can fit in Inventory
     * @return
     */
    public static boolean hasInventorySpace(Inventory inventory, ItemStack is) {
        Inventory inv = Bukkit.createInventory(null, inventory.getSize());

        for (int i = 0; i < inv.getSize(); i++) {
            if (inventory.getItem(i) != null) {
                ItemStack item = inventory.getItem(i).clone();
                inv.setItem(i, item);
            }
        }

        if (inv.addItem(is.clone()).size() > 0) {
            return false;
        }

        return true;
    }

    /**
     * Check the amount of slots available
     *
     * @param inv - Inventory to check the slots available
     * @return
     */
    public static int checkSlotsAvailable(Inventory inv) {
        ItemStack[] items = inv.getContents(); //Contents of player inventory
        int emptySlots = 0;

        for (ItemStack is : items) {
            if (is == null) {
                emptySlots = emptySlots + 1;
            }
        }

        return emptySlots;
    }

    /**
     * Check the amount of slots available
     *
     * @param player - Player passes to check avaliable slots
     * @return
     */
    public static int checkSlotsAvailable(Player player) {
        return checkSlotsAvailable(player.getInventory());
    }

    /**
     * remove items from players inventory PROPERLY
     * Credit to rbrick
     *
     * @param p        - Player to remove Material from
     * @param material - Material to remove
     * @param amount   - Amount to remove from Players Inventory
     */
    public static void removeItem(final Player p, final Material material, final int amount) {
        for (int i = 1; i <= amount; ++i) {
            final ItemStack ite = p.getInventory().getItem(p.getInventory().first(material));
            final ItemStack it = new ItemStack(ite.getType(), 1, ite.getDurability());
            it.setItemMeta(ite.getItemMeta());
            p.getInventory().removeItem(new ItemStack[]{it});
        }
    }

    public static void removeItem(final Player p, final Material material, short dur, final int amount) {
        for (int i = 1; i <= amount; ++i) {
            final ItemStack ite = p.getInventory().getItem(p.getInventory().first(material));
            final ItemStack it = new ItemStack(ite.getType(), 1, dur);
            it.setItemMeta(ite.getItemMeta());
            p.getInventory().removeItem(new ItemStack[]{it});
        }
    }

    /**
     *
     * @param player
     * @param safeRemove - if true, will only remove items without a lore/displayname
     * @param material
     * @param dur
     * @param amount
     */
    public static void removeItem(Player player, boolean safeRemove, Material material, short dur, int amount) {
        if(safeRemove) {
            int left = amount;

            for(ItemStack item : player.getInventory()) {
                if(item == null || item.getType() == Material.AIR || item.getType() != material || !isSafeItem(item)) continue;
                if(left <= 0) break;

                if(item.getAmount() > left) {
                    item.setAmount(item.getAmount() - left);
                    player.updateInventory();
                    left-=item.getAmount();
                    break;
                }
                else {
                    left-=item.getAmount();
                    player.getInventory().removeItem(item);
                }
            }
        }
        else {
            removeItem(player, material, dur, amount);
        }
    }

    public static boolean isSafeItem(ItemStack item) {
        return !item.hasItemMeta() || (!item.getItemMeta().hasDisplayName() && !item.getItemMeta().hasLore());
    }

    /**
     * Remove Check if player has X amount in inventory of Material
     *
     * @param i        - Inventory to check if it contains Material
     * @param material - Material to check if it's in Inventory
     * @param amount   - Amount to check in Inventory
     * @return
     */
    public static boolean containsAmount(Inventory i, Material material, int amount) {
        ItemStack[] contents = i.getContents();
        int count = 0;
        for (ItemStack item : contents) {
            if (item != null && item.getData().getItemType().equals(material)) {
                count = count + item.getAmount();
            }
        }
        return count >= amount;
    }

    /**
     * Get amount in players inventory of Material
     *
     * @param inv      - Inventory to check
     * @param material - Material to check the amount
     * @return
     */
    public static int getAmountInInventory(Inventory inv, Material material) {
        int amount = 0;
        ItemStack[] contents = inv.getContents();

        for (ItemStack item : contents) {
            if (item != null && item.getType() == material) {
                amount = amount + item.getAmount();
            }
        }

        return amount;
    }

    /**
     * Get amount in players inventory of Material
     *
     * @param inv      - Inventory to check
     * @param material - Material to check the amount
     * @return
     */
    public static int getAmountInInventory(Inventory inv, Material material, short data) {
        int amount = 0;
        ItemStack[] contents = inv.getContents();

        for (ItemStack item : contents) {
            if (item != null && item.getType() == material && item.getDurability() == data) {
                amount = amount + item.getAmount();
            }
        }

        return amount;
    }

    public static String getFriendlyItemStackName(ItemStack i) {
        String name = "UNKNOWN";

        if(i != null) {
            try {
                name = CraftItemStack.asNMSCopy(i).getName();
            } catch (Exception ex) {
                name = i.getType().toString();
            }
        }

        return name;
    }

    public static String getStrippedTitle(ItemStack item) {
        return item == null ? "" : item.hasItemMeta() ? item.getItemMeta().hasDisplayName() ? ChatColor.stripColor(item.getItemMeta().getDisplayName()).trim() : "" : "";
    }

    public static String getTitle(ItemStack item) {
        return item == null ? "" : item.hasItemMeta() ? item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName().trim() : "" : "";
    }

    public static ItemStack[] fixInventoryOrder(ItemStack[] source) {
        ItemStack[] fixed = new ItemStack[36];

        System.arraycopy(source, 0, fixed, 27, 9);
        System.arraycopy(source, 9, fixed, 0, 27);

        return fixed;
    }

}
