package me.sores.impulse.util.abstr;

import com.google.common.collect.Lists;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandRegistrar;
import me.sores.impulse.util.handler.Handler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Created by sores on 4/16/2021.
 */
public abstract class AbstractInit {

    private Plugin plugin;
    private CommandRegistrar commandRegistrar;
    private List<Handler> handlerList = Lists.newArrayList();

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

    public void initHandler(Handler handler, boolean listener){
        try{
            handlerList.add(handler);
            handler.init();
            if(listener) registerListener(handler);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public List<Handler> getHandlerList() {
        return handlerList;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public CommandRegistrar getCommandRegistrar() {
        return commandRegistrar;
    }

}
