package me.sores.impulse;

import me.sores.impulse.commands.Command_impulse;
import me.sores.impulse.listeners.Listener_playerlistener;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
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

        loadListeners();
        loadCommands();
        StringUtil.log("&a[Impulse] Impulse version &f" + instance.getDescription().getVersion() + " &asuccessfully loaded.");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void loadCommands(){
        new Command_impulse(this);
    }

    private void loadListeners(){
        Arrays.asList(new Listener_playerlistener(), new MenuListener()).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public static Impulse getInstance() {
        return instance;
    }
}
