package me.sores.spark.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by LavaisWatery on 2017-10-23.
 */
public class ItemData {

    public ItemData(Material mat) {
        this.material = mat;
        this.data = (short) 0;
    }

    public ItemData(Material mat, short data) {
        this.material = mat;
        this.data = data;
    }

    public ItemData(Material mat, short data, String display) {
        this.material = mat;
        this.data = data;
        this.display = display;
    }

    public ItemData(ItemStack item) {
        this.material = item.getType();
        this.data = item.getDurability();
        this.display = ItemUtil.getSafeDisplay(item);
        this.lores = item.hasItemMeta() ? item.getItemMeta().getLore() : null;
    }

    private Material material;
    private short data;
    private String display;
    private List<String> lores;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<String> getLores() {
        return lores;
    }

    public void setLores(List<String> lores) {
        this.lores = lores;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public short getData() {
        return data;
    }

    public void setData(short data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemData ? ((ItemData) obj).getData() == data && ((ItemData) obj).getMaterial() == material && ((lores == null && ((ItemData) obj).getLores() == null) || (lores != null && ((ItemData) obj).getLores() != null) && lores.containsAll(((ItemData) obj).getLores())) : false;
    }
}
