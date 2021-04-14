package me.sores.impulse.util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import java.util.List;

/**
 * Created by sores on 3/3/2020.
 */
public class ItemBuilder {

    private ItemStack item;


    public ItemBuilder(Material material) {
        item = new ItemStack(material);
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setData(short data){
        item.setDurability(data);
        return this;
    }

    public ItemBuilder setData(MaterialData data){
        item.setData(data);
        return this;
    }

    public ItemBuilder setLore(List<String> lore){
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setName(String name){
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color(name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder unsafeEnchant(Enchantment enchantment, int level){
        item.addUnsafeEnchantment(enchantment, level);

        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level){
        item.addEnchantment(enchantment, level);

        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment){
        ItemMeta meta = item.getItemMeta();

        if(meta.hasEnchants()){
            if(item.getEnchantments().containsKey(enchantment)){
                meta.removeEnchant(enchantment);

                item.setItemMeta(meta);
            }
        }

        return this;
    }

    public ItemBuilder clearEnchants(){
        ItemMeta meta = item.getItemMeta();

        if(meta.hasEnchants()){
            for(Enchantment enchantments : item.getEnchantments().keySet()){
                item.removeEnchantment(enchantments);
            }
        }

        return this;
    }

    public ItemBuilder color(Color color){
        if(item.getType().toString().contains("LEATHER_")){
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

            meta.setColor(color);
            item.setItemMeta(meta);
        }

        return this;
    }

    public ItemStack build(){
        return item;
    }
    
}
