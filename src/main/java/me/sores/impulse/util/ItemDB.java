package me.sores.impulse.util;

import me.sores.impulse.util.file.ManagedFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sores on 4/16/2021.
 */
public class ItemDB {

    private final transient Map<String, Integer> items = new HashMap<>();
    private final transient Map<ItemData, List<String>> names = new HashMap<>();
    private final transient Map<ItemData, String> primaryName = new HashMap<>();
    private final transient Map<String, Short> durabilities = new HashMap<>();
    private final transient ManagedFile file;
    private final transient Pattern splitPattern = Pattern.compile("((.*)[:+',;.](\\d+))");

    public ItemDB(Plugin plugin)
    {
        file = new ManagedFile("items.csv", plugin);
        reloadConfig();
    }

    public void reloadConfig()
    {
        final List<String> lines = file.getLines();

        if (lines.isEmpty())
        {
            return;
        }

        durabilities.clear();
        items.clear();
        names.clear();
        primaryName.clear();

        for (String line : lines)
        {
            line = line.trim().toLowerCase(Locale.ENGLISH);
            if (line.length() > 0 && line.charAt(0) == '#')
            {
                continue;
            }

            final String[] parts = line.split("[^a-z0-9]");
            if (parts.length < 2)
            {
                continue;
            }

            final int numeric = Integer.parseInt(parts[1]);
            final short data = parts.length > 2 && !parts[2].equals("0") ? Short.parseShort(parts[2]) : 0;
            String itemName = parts[0].toLowerCase(Locale.ENGLISH);

            durabilities.put(itemName, data);
            items.put(itemName, numeric);

            ItemData itemData = new ItemData(numeric, data);
            if (names.containsKey(itemData))
            {
                List<String> nameList = names.get(itemData);
                nameList.add(itemName);
                Collections.sort(nameList, new LengthCompare());
            }
            else
            {
                List<String> nameList = new ArrayList<String>();
                nameList.add(itemName);
                names.put(itemData, nameList);
                primaryName.put(itemData, itemName);
            }
        }
    }

    public ItemStack get(final String id, final int quantity) throws Exception
    {
        final ItemStack retval = get(id.toLowerCase(Locale.ENGLISH));
        retval.setAmount(quantity);
        return retval;
    }

    public ItemStack get(final String id) throws Exception
    {
        int itemid = 0;
        String itemname = null;
        short metaData = 0;
        Matcher parts = splitPattern.matcher(id);
        if (parts.matches())
        {
            itemname = parts.group(2);
            metaData = Short.parseShort(parts.group(3));
        }
        else
        {
            itemname = id;
        }

        if (NumberUtil.isNumber(itemname))
        {
            itemid = Integer.parseInt(itemname);
        }
        else if (NumberUtil.isNumber(id))
        {
            itemid = Integer.parseInt(id);
        }
        else
        {
            itemname = itemname.toLowerCase(Locale.ENGLISH);
        }

        if (itemid < 1)
        {
            if (items.containsKey(itemname))
            {
                itemid = items.get(itemname);
                if (durabilities.containsKey(itemname) && metaData == 0)
                {
                    metaData = durabilities.get(itemname);
                }
            }
            else if (Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH)) != null)
            {
                Material bMaterial = Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH));
                itemid = bMaterial.getId();
            }
            else
            {
                try
                {
                    Material bMaterial = Bukkit.getUnsafe().getMaterialFromInternalName(itemname.toLowerCase(Locale.ENGLISH));
                    itemid = bMaterial.getId();
                }
                catch (Throwable throwable)
                {
                    throw new Exception("Unknown item name.", throwable);
                }
            }
        }

        if (itemid < 1)
        {
            throw new Exception("Un");
        }

        final Material mat = Material.getMaterial(itemid);
        if (mat == null)
        {
            throw new Exception("Unknown item name.");
        }
        final ItemStack retval = new ItemStack(mat);
        retval.setAmount(mat.getMaxStackSize());
        retval.setDurability(metaData);
        return retval;
    }

    public List<ItemStack> getMatching(Player user, String[] args) throws Exception
    {
        List<ItemStack> is = new ArrayList<ItemStack>();

        if (args.length < 1)
        {
            is.add(user.getItemInHand());
        }
        else if (args[0].equalsIgnoreCase("hand"))
        {
            is.add(user.getItemInHand());
        }
        else if (args[0].equalsIgnoreCase("inventory") || args[0].equalsIgnoreCase("invent") || args[0].equalsIgnoreCase("all"))
        {
            for (ItemStack stack : user.getInventory().getContents())
            {
                if (stack == null || stack.getType() == Material.AIR)
                {
                    continue;
                }
                is.add(stack);
            }
        }
        else if (args[0].equalsIgnoreCase("blocks"))
        {
            for (ItemStack stack : user.getInventory().getContents())
            {
                if (stack == null || stack.getTypeId() > 255 || stack.getType() == Material.AIR)
                {
                    continue;
                }
                is.add(stack);
            }
        }
        else
        {
            is.add(get(args[0]));
        }

        if (is.isEmpty() || is.get(0).getType() == Material.AIR)
        {
            throw new Exception("Item air");
        }

        return is;
    }

    public String names(ItemStack item)
    {
        ItemData itemData = new ItemData(item.getTypeId(), item.getDurability());
        List<String> nameList = names.get(itemData);
        if (nameList == null)
        {
            itemData = new ItemData(item.getTypeId(), (short)0);
            nameList = names.get(itemData);
            if (nameList == null)
            {
                return null;
            }
        }

        if (nameList.size() > 15)
        {
            nameList = nameList.subList(0, 14);
        }
        return StringUtil.joinList(", ", nameList);
    }

    public String name(ItemStack item)
    {
        ItemData itemData = new ItemData(item.getTypeId(), item.getDurability());
        String name = primaryName.get(itemData);
        if (name == null)
        {
            itemData = new ItemData(item.getTypeId(), (short)0);
            name = primaryName.get(itemData);
            if (name == null)
            {
                return null;
            }
        }
        return name;
    }

    public String name(Material material) {
        ItemData itemData = new ItemData(material.getId(), (short) 0);
        String name = primaryName.get(itemData);
        if (name == null)
        {
            itemData = new ItemData(material.getId(), (short)0);
            name = primaryName.get(itemData);
            if (name == null)
            {
                return null;
            }
        }
        return name;
    }

    static class ItemData
    {
        final private int itemNo;
        final private short itemData;

        ItemData(final int itemNo, final short itemData)
        {
            this.itemNo = itemNo;
            this.itemData = itemData;
        }

        public int getItemNo()
        {
            return itemNo;
        }

        public short getItemData()
        {
            return itemData;
        }

        @Override
        public int hashCode()
        {
            return (31 * itemNo) ^ itemData;
        }

        @Override
        public boolean equals(Object o)
        {
            if (o == null)
            {
                return false;
            }
            if (!(o instanceof ItemData))
            {
                return false;
            }
            ItemData pairo = (ItemData)o;
            return this.itemNo == pairo.getItemNo()
                    && this.itemData == pairo.getItemData();
        }
    }


    class LengthCompare implements Comparator<String>
    {
        public LengthCompare()
        {
            super();
        }

        @Override
        public int compare(String s1, String s2)
        {
            return s1.length() - s2.length();
        }
    }

}
