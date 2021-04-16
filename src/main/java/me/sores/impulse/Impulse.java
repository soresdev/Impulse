package me.sores.impulse;

import me.sores.impulse.util.StringUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

/**
 * Created by sores on 3/3/2020.
 */
public class Impulse extends JavaPlugin {

    private static Impulse instance;

    public static Random RAND = new Random();

    @Override
    public void onEnable() {
        instance = this;

        new Init(this);

        StringUtil.log("&a[Impulse] Impulse version &f" + instance.getDescription().getVersion() + " &asuccessfully loaded.");
    }

    @Override
    public void onDisable() {
        Init.getInstance().unload();
        instance = null;
    }

    public static Impulse getInstance() {
        return instance;
    }
}
