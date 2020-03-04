package me.sores.spark;

import me.sores.spark.commands.Command_spark;
import me.sores.spark.listeners.Listener_playerlistener;
import me.sores.spark.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by sores on 3/3/2020.
 */
public class Spark extends JavaPlugin {

    private static Spark instance;

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
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new Listener_playerlistener(), this);
    }

    public static Spark getInstance() {
        return instance;
    }
}
