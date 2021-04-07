package me.sores.spark;

import me.sores.spark.commands.Command_spark;
import me.sores.spark.listeners.Listener_playerlistener;
import me.sores.spark.util.StringUtil;
import me.sores.spark.util.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by sores on 3/3/2020.
 */
public class Spark extends JavaPlugin {

    private static Spark instance;

    public static Random RAND = new Random();

    @Override
    public void onEnable() {
        instance = this;

        loadListeners();
        loadCommands();
        StringUtil.log("&a[Spark] Spark version &f" + instance.getDescription().getVersion() + " &asuccessfully loaded.");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void loadCommands(){
        new Command_spark(this);
    }

    private void loadListeners(){
        Arrays.asList(new Listener_playerlistener(), new MenuListener()).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public static Spark getInstance() {
        return instance;
    }
}
