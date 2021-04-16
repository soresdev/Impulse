package me.sores.impulse.util.abstr;

import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandRegistrar;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Created by sores on 4/16/2021.
 */
public abstract class AbstractInit {

    private Plugin plugin;
    private CommandRegistrar commandRegistrar;

    public AbstractInit(Plugin plugin) {
        this.plugin = plugin;
        this.commandRegistrar = new CommandRegistrar(plugin);
    }

    public abstract void initInstances();

    public abstract void registerEvents();

    public abstract void registerCommands();

    public abstract void unload() throws Exception;

    public void registerListener(Listener... list) {
        for(Listener l : list) {
            plugin.getServer().getPluginManager().registerEvents(l, plugin);
        }
    }

    public void registerCommand(CommandRegistrar commandRegister, String cmdName, BaseCommand command) {
        try {
            commandRegister.registerCommand(cmdName, command);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public CommandRegistrar getCommandRegistrar() {
        return commandRegistrar;
    }

}
